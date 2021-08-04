package com.ak47007.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * @date 2020/11/12 18:23
 * describe： 最近文章 View Object
 */
@Data
public class LatelyArticleVO {

    /**
     * 文章uuid
     */
    @ApiModelProperty(value = "文章uuid")
    private String uuid;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String title;

    /**
     * 文章封面
     */
    @ApiModelProperty(value = "文章封面")
    private String cover;

    /**
     * 文章描述
     */
    @ApiModelProperty(value = "文章描述")
    private String desc;

    /**
     * 文章标签
     */
    @ApiModelProperty(value = "文章标签")
    private String tag;

    /**
     * 文章发布时间
     */
    @ApiModelProperty(value = "文章发布时间")
    private String date;


}
