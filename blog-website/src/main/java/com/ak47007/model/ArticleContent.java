package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  AK47007
 * @date 2020/5/18
 * 
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "article_content")
public class ArticleContent implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章ID
     */
    @TableField(value = "article_id")
    private Long articleId;

    /**
     * 文章内容
     */
    @TableField(value = "article_content")
    private String articleContent;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_ARTICLE_ID = "article_id";

    public static final String COL_ARTICLE_CONTENT = "article_content";
}