package com.ak47007.mapper;

import com.ak47007.model.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author AK47007
 * date 2021/5/11 20:52
 * describes:
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 获得总访问数
     *
     * @param userId 用户ID
     */
    Integer getSumViews(@Param("userId") long userId);

    /**
     * 查询该文章下的标签
     *
     * @param tagId 标签ID
     */
    int tagCount(Long tagId);

    /**
     * 文章饼图
     */
    List<Article> getArticlePie(@Param("userId") Long userId);

    /**
     * 根据发布月份查询文章
     */
    List<Article> findLatelyArticle(@Param("userId") long userId);



}