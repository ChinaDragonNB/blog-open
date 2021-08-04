package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *@author ak47007
 *@date 2020/5/23
 *描述： 文章标签中间表
 */

@Data
@TableName(value = "article_tag")
public class ArticleTag {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    @TableField(value = "article_id")
    private Long articleId;

    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    @TableField(value = "tag_id")
    private Long tagId;

    public static final String COL_ID = "id";

    public static final String COL_ARTICLE_ID = "article_id";

    public static final String COL_TAG_ID = "tag_id";
}