package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName(value = "`article_content`")
public class ArticleContent {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章ID
     */
    @ApiModelProperty(value = "文章ID")
    @TableField(value = "`article_id`")
    private Long articleId;

    /**
     * 文章内容
     */
    @ApiModelProperty(value = "文章内容")
    @TableField(value = "`article_content`")
    private String articleContent;

    public static final String COL_ID = "id";

    public static final String COL_ARTICLE_ID = "article_id";

    public static final String COL_ARTICLE_CONTENT = "article_content";
}