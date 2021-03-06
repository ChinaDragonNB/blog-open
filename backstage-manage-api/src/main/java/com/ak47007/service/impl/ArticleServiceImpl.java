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
        // ????????????
        if (Strings.isNotBlank(query.getTitle())) {
            wrapper.like(ViewArticleList::getTitle, query.getTitle());
        }
        // ????????????
        if (query.getPublishTimeStart() != null) {
            wrapper.ge(ViewArticleList::getPublishTime, query.getPublishTimeStart());
        }
        // ????????????
        if (query.getPublishTimeEnd() != null) {
            // ???????????????????????????????????????,00:00:00 ???23:59:59 ?????????
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
        AssertUtil.notNull(article, "????????????????????????");

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
            return Result.error("???????????????");
        }
        Article article = dto.getArticle();
        Boolean isCheckOks = dto.getIsCheckOks();
        Long userId = dto.getUserId();
        int result;
        String markdown = article.getMarkdown();
        Long articleId = article.getId();
        switch (type) {
            case 1:
                //????????????
                article.setPublishTime(LocalDateTime.now());
                //UUID
                article.setUuid(ShortStrUtil.shortStr(UUID.randomUUID().toString()));
                //??????
                article.setUserId(userId);
                result = articleMapper.insert(article);
                if (result > 0) {
                    articleId = article.getId();
                    ArticleContent articleContent = new ArticleContent();
                    articleContent.setArticleId(articleId);
                    articleContent.setArticleContent(markdown);
                    articleContentMapper.insert(articleContent);
                    articleTagService.insertBatchData(articleId, tagIds);
                    // ??????Redis??????????????????
                    restLatelyArticle(userId);
                    return Result.success("????????????", articleId);
                }
                return Result.error("????????????");
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
                    // ??????Redis??????????????????
                    restLatelyArticle(userId);
                    return Result.success("????????????", articleId);
                }
                return Result.error("????????????");
            default:
                return Result.error("????????????");
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
            // ????????????????????????????????????????????????????????????,???????????????Map?????????
            return IntStream.range(0, articlePieList.size()).mapToObj(i -> {
                String title = articlePieList.get(i).getTitle();
                Integer y = articlePieList.get(i).getViews();
                boolean sliced = false;
                boolean selected = false;
                // ?????????????????????
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
        // ???????????????????????????????????????
        articleContentMapper.deleteByArticleId(articleId);
        // ???????????????????????????????????????
        articleTagMapper.deleteByArticleId(articleId);
        // ????????????
        int result = articleMapper.deleteById(articleId);
        if (result > 0) {
            // ??????Redis??????????????????
            restLatelyArticle(userId);
            return Result.success("????????????");
        }
        return Result.error("????????????");
    }

    @Override
    public Result<?> uploadMarkDown(MultipartFile file, SysUser user) {
        String fileName = file.getOriginalFilename();

        if (Strings.isNotBlank(fileName)) {
            // ????????????
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (Strings.isNotBlank(fileType)) {
                // ??????md??????
                if (!fileType.equals("md")) {
                    return Result.error("????????????,?????????MarkDown??????");
                }
            } else {
                return Result.error("????????????,?????????MarkDown??????");
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
            return Result.error("????????????,????????????:" + e.getMessage());
        }
        // ??????????????????
        if (Strings.isBlank(sb.toString())) {
            return Result.error("???????????????????????????,???????????????????????????");
        }
        if (StrUtil.isBlank(fileName)) {
            return Result.error("?????????????????????");
        }
        // ???????????????????????????
        String title = fileName.substring(0, fileName.lastIndexOf("."));
        LambdaQueryWrapper<Article> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Article::getTitle, title);
        Article article = articleMapper.selectOne(wrapper);

        // ????????????
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

            return Result.success("??????????????????");
        }
        // ???????????????
        else {
            article.setMarkdown(markdown);
            // ??????????????????????????????redis
            String redisKey = SecureUtil.md5(user.getUserName() + "articleId=" + article.getId()).toUpperCase();
            redisService.setObject(redisKey, article, 60);
            return Result.success("????????????,????????????????????????,????????????????????????", article.getId());
        }
    }

    @Override
    public Result<?> replaceArticleContent(Long articleId, SysUser user) {
        // ???redis???????????????
        Article value = null;
        try {
            String redisKey = SecureUtil.md5(user.getUserName() + "articleId=" + articleId).toUpperCase();
            value = redisService.getValue(redisKey, Article.class);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("????????????,????????????:" + e.getMessage());
        }
        articleContentMapper.updateArticleContentByArticleId(value.getMarkdown(), articleId);
        return Result.success("????????????");
    }


    /**
     * ?????????????????????Redis
     *
     * @param userId ??????ID
     * @return ????????????
     */
    public List<LatelyArticleVO> saveLatelyArticle(long userId) {
        String latelyArticleKey = getLatelyArticleKey(userId);
        // ????????????12?????????
        List<Article> articleList = articleMapper.findLatelyArticle(userId);
        // ??????
        LocalDateTime now = LocalDateTime.now();
        List<LatelyArticleVO> data = articleList.stream().map(l -> {
            LatelyArticleVO vo = new LatelyArticleVO();
            vo.setUuid(l.getUuid());
            vo.setTitle(l.getTitle());
            vo.setCover(l.getCover());
            vo.setDesc(l.getArticleDescribe());
            vo.setTag(l.getTagName());
            // ??????????????????
            LocalDateTime publishTime = l.getPublishTime();
            String date = "??????";
            Duration duration = Duration.between(publishTime, now);
            //??????????????????
            long minutes = duration.toMinutes();
            //??????????????????
            long hours = duration.toHours();
            //???????????????
            long days = duration.toDays();
            if (minutes >= 1 && minutes <= 60) {
                date = minutes + "?????????";
            } else if (hours >= 1 && hours <= 24) {
                date = hours + "?????????";
            } else if (days >= 1) {
                date = days + "??????";
            }
            vo.setDate(date);
            return vo;
        }).collect(Collectors.toList());
        try {
            redisService.setObject(latelyArticleKey, data, RedisConstant.ARTICLE_SAVE_TIME);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("?????????????????????Redis?????????,%s", e.getMessage());
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
     * ??????????????????Key
     *
     * @param userId ??????ID
     * @return key
     */
    private String getLatelyArticleKey(long userId) {
        return SecureUtil.md5("latelyArticle" + userId).toUpperCase();
    }


}




