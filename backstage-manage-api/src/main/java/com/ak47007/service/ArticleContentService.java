package com.ak47007.service;

import com.ak47007.model.ArticleContent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author ak47007
 * @date 2020/5/23
 * 描述：
 */
public interface ArticleContentService extends IService<ArticleContent> {

    /**
     * 获取文章内容
     *
     * @param articleId 文章id
     * @return 文章内容
     */
    String getArticleContentById(long articleId);

}
