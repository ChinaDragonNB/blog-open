package com.ak47007.mapper;

import com.ak47007.model.ArticleContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface ArticleContentMapper extends BaseMapper<ArticleContent> {
    /**
     * 根据文章id删除
     *
     * @param articleId 文章ID
     */
    int deleteByArticleId(@Param("articleId") Long articleId);

    /**
     * 根据文章id修改内容
     *
     * @param updatedArticleContent 文章内容
     * @param articleId             文章id
     */
    int updateArticleContentByArticleId(@Param("updatedArticleContent") String updatedArticleContent, @Param("articleId") Long articleId);
}