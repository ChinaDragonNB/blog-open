package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AK47007
 * @date 2020/5/18
 */

/**
 * 文章表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "article")
public class Article implements Serializable {
    public static final String COL_DESCRIBE = "describe";
    public static final String COL_MARKDOWN = "markdown";
    public static final String COL_VIEWS = "views";
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * guid
     */
    @TableField(value = "uuid")
    private String uuid;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 封面图片
     */
    @TableField(value = "cover")
    private String cover;

    /**
     * 文章描述
     */
    @TableField(value = "article_describe")
    private String articleDescribe;

    /**
     * 发布时间
     */
    @TableField(value = "publish_time")
    private LocalDateTime publishTime;

    /**
     * 修改时间
     */
    @TableField(value = "edit_time")
    private LocalDateTime editTime;

    /**
     * 标签
     */
    @TableField(value = "tag_id")
    private Long tagId;

    /**
     * 发布状态：1.public  2.private
     */
    @TableField(value = "state")
    private Integer state;

    /**
     * 是否置顶 1:是 0 否
     */
    @TableField(value = "is_stickie")
    private Integer isStickie;

    /**
     * 文章类型,1 原创 2 转载 3 翻译
     */
    @TableField(value = "article_type")
    private Integer articleType;

    /**
     * 文章作者
     */
    @TableField(value = "user_id")
    private Long userId;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_UUID = "uuid";

    public static final String COL_TITLE = "title";

    public static final String COL_COVER = "cover";

    public static final String COL_ARTICLE_DESCRIBE = "article_describe";

    public static final String COL_PUBLISH_TIME = "publish_time";

    public static final String COL_EDIT_TIME = "edit_time";

    public static final String COL_TAG_ID = "tag_id";

    public static final String COL_STATE = "state";

    public static final String COL_IS_STICKIE = "is_stickie";

    public static final String COL_ARTICLE_TYPE = "article_type";

    public static final String COL_USER_ID = "user_id";

    public static ArticleBuilder builder() {
        return new ArticleBuilder();
    }
}