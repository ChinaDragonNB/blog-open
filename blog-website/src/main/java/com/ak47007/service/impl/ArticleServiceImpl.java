package com.ak47007.service.impl;

import com.ak47007.mapper.*;
import com.ak47007.model.ArticleTag;
import com.ak47007.model.BrowseRecord;
import com.ak47007.model.Tag;
import com.ak47007.model.query.ArticleQuery;
import com.ak47007.model.vo.ArticleVO;
import com.ak47007.model.vo.ViewArticleCard;
import com.ak47007.service.ArticleService;
import com.ak47007.util.LocationUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ViewArticleCardMapper viewArticleCardMapper;

    private final ArticleTagMapper articleTagMapper;

    private final TagMapper tagMapper;

    private final ArticleVOMapper articleVOMapper;

    private final BrowseRecordMapper browseRecordMapper;

    private final LocationUtils locationUtils;


    @Override
    public ModelAndView getData(int pageNum, String tag, String title) {
        ModelAndView modelAndView = new ModelAndView();
        if (pageNum < 1) {
            modelAndView.setViewName("error/404");
            return modelAndView;
        }

        // 默认页数
        ArticleQuery articleQuery = new ArticleQuery();

        // 条件查询不分页
        if (Strings.isBlank(tag) && Strings.isBlank(title)) {
            articleQuery.setPageNum(pageNum);
            articleQuery.setPageSize(ArticleQuery.PAGE_SIZE);
        } else {
            articleQuery.setTagName(tag);
            articleQuery.setTitle(title);
        }
        List<ViewArticleCard> articleList = getArticleList(articleQuery);
        modelAndView.addObject("isNull", articleList.isEmpty());
        modelAndView.setViewName("article/article");
        if (articleList.isEmpty()) {

            return modelAndView;
        }
        PageInfo<ViewArticleCard> pageInfo = new PageInfo<>(articleList);
        // 总页数
        int pages = pageInfo.getPages();
        if (pageNum > pages) {
            modelAndView.setViewName("error/404");
            return modelAndView;
        }
        modelAndView.addObject("response", pageInfo);
        return modelAndView;
    }

    @Override
    public List<ViewArticleCard> getArticleList(ArticleQuery articleQuery) {

        // 查询文章条件过滤
        LambdaQueryWrapper<ViewArticleCard> wrapper = Wrappers.lambdaQuery();


        // 查询文章所关联的标签
        List<ArticleTag> articleTags = articleTagMapper.selectList(null);

        // 查询所有标签
        List<Tag> tags = tagMapper.selectList(null);


        if (Strings.isNotBlank(articleQuery.getTagName())) {
            // 根据标签名查询标签
            Tag tag = tags.stream().filter(tg -> tg.getTagName().equals(articleQuery.getTagName()))
                    .findFirst().orElse(new Tag());

            // 文章ID集合
            List<Long> articleIds = articleTags.stream()
                    .filter(articleTag -> articleTag.getTagId().equals(tag.getTagId()))
                    .map(ArticleTag::getArticleId).collect(Collectors.toList());

            // 如果有的话筛选所关联的文章,如果没有就按文章分配的标签来查询
            if (!articleIds.isEmpty()) {
                wrapper.in(ViewArticleCard::getId, articleIds);
            } else {
                wrapper.eq(ViewArticleCard::getTagName, articleQuery.getTagName());
            }
        }
        if (Strings.isNotBlank(articleQuery.getTitle())) {
            // 标题查询区分大小写
            wrapper.like(ViewArticleCard::getTitle, articleQuery.getTitle());
        }
        // 分页
        if (articleQuery.getPageNum() != null && articleQuery.getPageSize() != null) {
            PageHelper.startPage(articleQuery.getPageNum(), articleQuery.getPageSize());
        }

        List<ViewArticleCard> articles = viewArticleCardMapper.selectList(wrapper);

        // 找到所关联的标签
        articleTags.forEach(articleTag -> {
            Tag obj = tags.stream().filter(tag -> tag.getTagId().equals(articleTag.getTagId())).findFirst().orElse(null);
            if (obj != null) {
                articleTag.setTagName(obj.getTagName());
                articleTag.setTagLink(obj.getTagLogo());
            }
        });
        articles.forEach(article -> article.setArticleTagList(articleTags.stream()
                .filter(articleTag -> articleTag.getArticleId().equals(article.getId())).collect(Collectors.toList())));
        return articles;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleVO articleDetail(String uuid, String ip) {
        // 获取文章信息
        ArticleVO articleInfo = getArticleInfo(uuid);
        // 浏览记录
        BrowseRecord browseRecord = new BrowseRecord();
        browseRecord.setArticleId(articleInfo.getId());
        browseRecord.setBrowseIp(ip);
        browseRecord.setBrowseTime(LocalDateTime.now());
        browseRecord.setIpLocation(locationUtils.getPlace(ip));
        int insert = browseRecordMapper.insert(browseRecord);
        if (insert > 0) {
            articleInfo.setCountView(articleInfo.getCountView() + 1);
        }
        return articleInfo;
    }

    @Override
    public ArticleVO getArticleInfo(String uuid) {
        LambdaQueryWrapper<ArticleVO> wrapper = Wrappers.lambdaQuery();
        // 公共的
        wrapper.eq(ArticleVO::getState, 1);
        wrapper.eq(ArticleVO::getUuid, uuid);
        ArticleVO articleVO = articleVOMapper.selectOne(wrapper);
        if (articleVO != null) {
            // 时间格式化
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
            articleVO.setAddTime(dtf.format(articleVO.getPublishTime()));
            articleVO.setUpdateTime(dtf.format(articleVO.getEditTime()));

            // 文章类型 1 原创 2转载 3翻译
            String articleTypeName = "";
            switch (articleVO.getArticleType()) {
                case 1:
                    articleTypeName = "原";
                    break;
                case 2:
                    articleTypeName = "转";
                    break;
                case 3:
                    articleTypeName = "翻";
                    break;
                default:
                    articleTypeName = "无";
                    break;
            }
            articleVO.setArticleTypeName(articleTypeName);
        }
        return articleVO;
    }
}
