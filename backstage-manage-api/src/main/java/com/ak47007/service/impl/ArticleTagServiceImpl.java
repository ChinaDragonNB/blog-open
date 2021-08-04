package com.ak47007.service.impl;

import com.ak47007.model.ArticleTag;
import com.ak47007.service.ArticleTagService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.ak47007.mapper.ArticleTagMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AK47007
 * @date 2019/7/13
 */
@Service
@AllArgsConstructor
public class ArticleTagServiceImpl implements ArticleTagService {

    private final ArticleTagMapper articleTagMapper;


    @Override
    public List<Long> haveTags(Long articleId) {
        LambdaQueryWrapper<ArticleTag> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ArticleTag::getArticleId, articleId);
        List<ArticleTag> articleTags = articleTagMapper.selectList(wrapper);
        return articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
    }

    @Override
    public void insertBatchData(Long articleId, List<Long> tagIds) {
        // 该文章现有的标签
        List<Long> existTagList = articleTagMapper.findTagByArticleId(articleId);
        for (Long tagId : tagIds) {
            Long existTadId = existTagList.stream().filter(l -> l.equals(tagId)).findFirst().orElse(null);
            // 不存在就新增
            if (existTadId == null) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(tagId);
                articleTagMapper.insert(articleTag);
            }
        }
    }


}

