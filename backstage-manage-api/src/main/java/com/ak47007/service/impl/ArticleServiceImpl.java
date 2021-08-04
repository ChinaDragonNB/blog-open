package com.ak47007.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.ak47007.constant.RedisConstant;
import com.ak47007.mapper.*;
import com.ak47007.model.*;
import com.ak47007.model.dto.ArticleDTO;
import com.ak47007.model.query.ArticleQuery;
import com.ak47007.model.vo.LatelyArticleVO;
import com.ak47007.model.base.Result;
import com.ak47007.model.vo.ViewArticleList;
import com.ak47007.model.vo.main.ArticlePieVO;
import com.ak47007.redis.RedisService;
import com.ak47007.service.ArticleService;
import com.ak47007.service.ArticleTagService;
import com.ak47007.utils.AssertUtil;
import com.ak47007.utils.ElementUISortUtil;
import com.ak47007.utils.ShortStrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author AK47007
 * @date 2019/7/13
 */
@Service
@Slf4j
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;

    private final ArticleContentMapper articleContentMapper;

    private final ArticleTagMapper articleTagMapper;

    private final ViewArticleListMapper viewArticleListMapper;

    private final RedisService redisService;

    private final SysUserMapper sysUserMapper;

    private final ArticleTagService articleTagService;

    @Override
    public List<ViewArticleList> list(ArticleQuery query) {

        LambdaQueryWrapper<ViewArticleList> wrapper = Wrappers.lambdaQuery();

        wrapper.eq(ViewArticleList::getUserId, query.getUserId());
        // 标题查询
        if (Strings.isNotBlank(query.getTitle())) {
            wrapper.like(ViewArticleList::getTitle, query.getTitle());
        }
        // 开始时间
        if (query.getPublishTimeStart() != null) {
            wrapper.ge(ViewArticleList::getPublishTime, query.getPublishTimeStart());
        }
        // 结束时间
        if (query.getPublishTimeEnd() != null) {
            // 这里需要加一天才能查询成功,00:00:00 与23:59:59 的区别
            wrapper.le(ViewArticleList::getPublishTime, query.getPublishTimeEnd().plusDays(1));
        }
        String sql = ElementUISortUtil.sortSql(query.getColumnName(), query.getOrder(), "state DESC, id DESC");
        wrapper.last(sql);

        query.startPage();
        List<ViewArticleList> articleLists = viewArticleListMapper.selectList(wrapper);
        return articleLists;
    }

    @Override
    public Article getInfo(Long id) {
        Article article = articleMapper.selectById(id);
        AssertUtil.notNull(article, "未找到该文章信息");

        LambdaQueryWrapper<ArticleContent> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ArticleContent::getArticleId, id);
        ArticleContent articleContent = articleContentMapper.selectOne(wrapper);
        if (articleContent != null) {
            article.setMarkdown(articleContent.getArticleContent());
        }
        return article;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> save(int type, ArticleDTO dto) {

        List<Long> tagIds = dto.getTagIds();
        if (tagIds.isEmpty()) {
            return Result.error("请选择标签");
        }
        Article article = dto.getArticle();
        Boolean isCheckOks = dto.getIsCheckOks();
        Long userId = dto.getUserId();
        int result;
        String markdown = article.getMarkdown();
        Long articleId = article.getId();
        switch (type) {
            case 1:
                //发布时间
                article.setPublishTime(LocalDateTime.now());
                //UUID
                article.setUuid(ShortStrUtil.shortStr(UUID.randomUUID().toString()));
                //作者
                article.setUserId(userId);
                result = articleMapper.insert(article);
                if (result > 0) {
                    articleId = article.getId();
                    ArticleContent articleContent = new ArticleContent();
                    articleContent.setArticleId(articleId);
                    articleContent.setArticleContent(markdown);
                    articleContentMapper.insert(articleContent);
                    articleTagService.insertBatchData(articleId, tagIds);
                    // 重置Redis中的最近文章
                    restLatelyArticle(userId);
                    return Result.success("保存成功", articleId);
                }
                return Result.error("保存失败");
            case 2:
                result = articleMapper.updateById(article);
                if (result > 0) {
                    ArticleContent articleContent = getArticleContent(article);
                    if (articleContent == null) {
                        articleContent = new ArticleContent();
                        articleContent.setArticleId(articleId);
                        articleContent.setArticleContent(markdown);
                        articleContentMapper.insert(articleContent);
                        articleContent.setId(articleContent.getId());
                    }
                    articleContent.setArticleId(articleId);
                    articleContent.setArticleContent(markdown);
                    articleContentMapper.updateById(articleContent);
                    if (isCheckOks) {
                        articleTagService.insertBatchData(articleId, tagIds);
                        articleTagMapper.deleteBatch(articleId, tagIds);
                    }
                    // 重置Redis中的最近文章
                    restLatelyArticle(userId);
                    return Result.success("保存成功", articleId);
                }
                return Result.error("保存失败");
            default:
                return Result.error("非法操作");
        }
    }


    public ArticleContent getArticleContent(Article record) {
        return articleContentMapper.selectOne(new QueryWrapper<ArticleContent>().eq(ArticleContent.COL_ARTICLE_ID, record.getId()));
    }


    @Override
    public int getCount(long userId) {
        LambdaQueryWrapper<Article> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Article::getUserId, userId);
        return articleMapper.selectCount(wrapper);
    }

    @Override
    public Integer getSumViews(long userId) {
        return articleMapper.getSumViews(userId);
    }

    @Override
    public List<ArticlePieVO> getArticlePie(Long userId) {
        List<Article> articlePieList = articleMapper.getArticlePie(userId);
        if (articlePieList != null && articlePieList.size() > 0) {
            // 因为实体类返回的属性与图表的属性对应不上,故此需要用Map转一下
            return IntStream.range(0, articlePieList.size()).mapToObj(i -> {
                String title = articlePieList.get(i).getTitle();
                Integer y = articlePieList.get(i).getViews();
                boolean sliced = false;
                boolean selected = false;
                // 第一个突出显示
                if (i == 0) {
                    sliced = true;
                    selected = true;
                }
                return new ArticlePieVO(title, y, sliced, selected);
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deleteArticle(long articleId, long userId) {
        // 删除文章与文章内容表的数据
        articleContentMapper.deleteByArticleId(articleId);
        // 删除文章与文章标签表的数据
        articleTagMapper.deleteByArticleId(articleId);
        // 删除文章
        int result = articleMapper.deleteById(articleId);
        if (result > 0) {
            // 重置Redis中的最近文章
            restLatelyArticle(userId);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @Override
    public Result<?> uploadMarkDown(MultipartFile file, SysUser user) {
        String fileName = file.getOriginalFilename();

        if (Strings.isNotBlank(fileName)) {
            // 文件类型
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (Strings.isNotBlank(fileType)) {
                // 不是md文件
                if (!fileType.equals("md")) {
                    return Result.error("导入失败,请上传MarkDown文件");
                }
            } else {
                return Result.error("导入失败,请上传MarkDown文件");
            }

        }
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            InputStream inputStream = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("导入失败,出现异常:" + e.getMessage());
        }
        // 文件内容为空
        if (Strings.isBlank(sb.toString())) {
            return Result.error("检测到文件没有内容,请写入内容后再上传");
        }
        if (StrUtil.isBlank(fileName)) {
            return Result.error("文件名不能为空");
        }
        // 文件名作为文章标题
        String title = fileName.substring(0, fileName.lastIndexOf("."));
        LambdaQueryWrapper<Article> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Article::getTitle, title);
        Article article = articleMapper.selectOne(wrapper);

        // 文章内容
        String markdown = sb.toString();
        if (article == null) {
            article = new Article();
            article.setUuid(ShortStrUtil.shortStr(UUID.randomUUID().toString()));
            article.setTitle(title);
            article.setCover("");
            article.setArticleDescribe("");
            article.setPublishTime(LocalDateTime.now());
            article.setTagId(25L);
            article.setState(1);
            article.setIsStickie(0);
            article.setArticleType(1);
            article.setUserId(user.getId());
            articleMapper.insert(article);

            ArticleContent articleContent = new ArticleContent();
            articleContent.setArticleId(article.getId());
            articleContent.setArticleContent(markdown);
            articleContentMapper.insert(articleContent);

            return Result.success("文章导入成功");
        }
        // 已存在文章
        else {
            article.setMarkdown(markdown);
            // 将文章内容暂时保存到redis
            String redisKey = SecureUtil.md5(user.getUserName() + "articleId=" + article.getId()).toUpperCase();
            redisService.setObject(redisKey, article, 60);
            return Result.success("上传成功,但发现文章已存在,是否覆盖文章内容", article.getId());
        }
    }

    @Override
    public Result<?> replaceArticleContent(Long articleId, SysUser user) {
        // 从redis中读取内容
        Article value = null;
        try {
            String redisKey = SecureUtil.md5(user.getUserName() + "articleId=" + articleId).toUpperCase();
            value = redisService.getValue(redisKey, Article.class);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("替换失败,异常消息:" + e.getMessage());
        }
        articleContentMapper.updateArticleContentByArticleId(value.getMarkdown(), articleId);
        return Result.success("替换成功");
    }


    /**
     * 保存最近文章至Redis
     *
     * @param userId 用户ID
     * @return 最近文章
     */
    public List<LatelyArticleVO> saveLatelyArticle(long userId) {
        String latelyArticleKey = getLatelyArticleKey(userId);
        // 查询最近12篇文章
        List<Article> articleList = articleMapper.findLatelyArticle(userId);
        // 现在
        LocalDateTime now = LocalDateTime.now();
        List<LatelyArticleVO> data = articleList.stream().map(l -> {
            LatelyArticleVO vo = new LatelyArticleVO();
            vo.setUuid(l.getUuid());
            vo.setTitle(l.getTitle());
            vo.setCover(l.getCover());
            vo.setDesc(l.getArticleDescribe());
            vo.setTag(l.getTagName());
            // 文章发布时间
            LocalDateTime publishTime = l.getPublishTime();
            String date = "刚刚";
            Duration duration = Duration.between(publishTime, now);
            //相差的分钟数
            long minutes = duration.toMinutes();
            //相差的小时数
            long hours = duration.toHours();
            //相差的天数
            long days = duration.toDays();
            if (minutes >= 1 && minutes <= 60) {
                date = minutes + "分钟前";
            } else if (hours >= 1 && hours <= 24) {
                date = hours + "小时前";
            } else if (days >= 1) {
                date = days + "天前";
            }
            vo.setDate(date);
            return vo;
        }).collect(Collectors.toList());
        try {
            redisService.setObject(latelyArticleKey, data, RedisConstant.ARTICLE_SAVE_TIME);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("最近文章保存至Redis中失败,%s", e.getMessage());
        }
        return data;
    }

    @Override
    public List<LatelyArticleVO> getLatelyArticle(Long userId) {
        String latelyArticleKey = getLatelyArticleKey(userId);
        List<LatelyArticleVO> latelyArticleList = redisService.getListValue(latelyArticleKey, LatelyArticleVO.class);
        if (latelyArticleList.isEmpty()) {
            latelyArticleList = saveLatelyArticle(userId);
        }
        return latelyArticleList;
    }


    private void restLatelyArticle(long userId) {
        String latelyArticleKey = getLatelyArticleKey(userId);
        redisService.delValue(latelyArticleKey);
    }

    /**
     * 获取最近文章Key
     *
     * @param userId 用户ID
     * @return key
     */
    private String getLatelyArticleKey(long userId) {
        return SecureUtil.md5("latelyArticle" + userId).toUpperCase();
    }


}




