package com.ak47007.service;

import java.util.List;

/**
 * @author AK47007
 * @date 2019/7/13
 */
public interface ArticleTagService {


    /**
     * 文章拥有的标签
     *
     * @param articleId 文章id
     */
    List<Long> haveTags(Long articleId);

    /**
     * 插入多条数据
     * @param articleId 文章ID
     * @param tagIds 标签ID集合
     */
    void insertBatchData(Long articleId, List<Long> tagIds);

}

