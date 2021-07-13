package com.ak47007.model.query;

import lombok.Data;

/**
 * @author AK47007
 * @date 2020/5/18
 */
@Data
public class ArticleQuery {

    /**
     * 当前页
     */
    private Integer pageNum;

    /**
     * 每页的数量
     */
    private Integer pageSize;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标题
     */
    private String title;

    /**
     * 默认每页显示的数量
     */
    public final static int PAGE_SIZE = 8;



}
