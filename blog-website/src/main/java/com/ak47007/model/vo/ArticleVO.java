package com.ak47007.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AK47007
 * @date 2020/5/19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "view_tb_article")
public class ArticleVO implements Serializable {
    @TableField(value = "id")
    private Long id;

    @TableField(value = "uuid")
    private String uuid;

    @TableField(value = "cover")
    private String cover;

    @TableField(value = "title")
    private String title;

    @TableField(value = "article_describe")
    private String articleDescribe;

    @TableField(value = "publish_time")
    private LocalDateTime publishTime;


    @TableField(value = "edit_time")
    private LocalDateTime editTime;

    @TableField(value = "tag_id")
    private Long tagId;

    @TableField(value = "state")
    private Integer state;

    @TableField(value = "is_stickie")
    private Integer isStickie;

    @TableField(value = "article_type")
    private Integer articleType;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "article_content")
    private String articleContent;

    @TableField(value = "count_view")
    private Long countView;

    @TableField(value = "count_comment")
    private Long countComment;

    /**
     * 发布时间格式化
     */
    @TableField(exist = false)
    private String addTime;

    /**
     * 更新时间格式化
     */
    @TableField(exist = false)
    private String updateTime;

    /**
     * 文章类型
     */
    @TableField(exist = false)
    private String articleTypeName;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_UUID = "uuid";

    public static final String COL_COVER = "cover";

    public static final String COL_TITLE = "title";

    public static final String COL_ARTICLE_DESCRIBE = "article_describe";

    public static final String COL_PUBLISH_TIME = "publish_time";

    public static final String COL_EDIT_TIME = "edit_time";

    public static final String COL_TAG_ID = "tag_id";

    public static final String COL_STATE = "state";

    public static final String COL_IS_STICKIE = "is_stickie";

    public static final String COL_ARTICLE_TYPE = "article_type";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_ARTICLE_CONTENT = "article_content";

    public static final String COL_COUNT_VIEW = "count_view";

    public static final String COL_COUNT_COMMENT = "count_comment";

    public static ArticleVOBuilder builder() {
        return new ArticleVOBuilder();
    }
}