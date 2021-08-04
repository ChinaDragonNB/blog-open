package com.ak47007.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ak47007.mapper.ArticleContentMapper;
import com.ak47007.model.ArticleContent;
import com.ak47007.service.ArticleContentService;

/**
 * @author ak47007
 * @date 2020/5/23
 * 描述：
 */
@Service
@AllArgsConstructor
public class ArticleContentServiceImpl extends ServiceImpl<ArticleContentMapper, ArticleContent> implements ArticleContentService {

    private final ArticleContentMapper articleContentMapper;

    @Override
    public String getArticleContentById(long articleId) {
        LambdaQueryWrapper<ArticleContent> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ArticleContent::getArticleId, articleId);
        ArticleContent articleContent = articleContentMapper.selectOne(wrapper);
        if (articleContent != null) {
            return articleContent.getArticleContent();
        }
        return "";
    }
}
