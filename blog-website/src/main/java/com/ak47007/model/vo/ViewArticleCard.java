package com.ak47007.model.vo;

import com.ak47007.model.ArticleTag;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AK47007
 * @date 2020/5/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "view_article_card")
public class ViewArticleCard implements Serializable {
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
    private LocalDate publishTime;

    @TableField(value = "edit_time")
    private LocalDate editTime;

    @TableField(value = "tag_id")
    private Long tagId;

    @TableField(value = "tag_name")
    private String tagName;

    @TableField(value = "state")
    private Integer state;

    @TableField(value = "is_stickie")
    private Integer isStickie;

    @TableField(value = "article_type")
    private Integer articleType;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "count_view")
    private Long countView;

    @TableField(value = "count_comment")
    private Long countComment;

    @TableField(exist = false)
    private List<ArticleTag> articleTagList;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_UUID = "uuid";

    public static final String COL_COVER = "cover";

    public static final String COL_TITLE = "title";

    public static final String COL_ARTICLE_DESCRIBE = "article_describe";

    public static final String COL_PUBLISH_TIME = "publish_time";

    public static final String COL_EDIT_TIME = "edit_time";

    public static final String COL_TAG_ID = "tag_id";

    public static final String COL_TAG_NAME = "tag_name";

    public static final String COL_STATE = "state";

    public static final String COL_IS_STICKIE = "is_stickie";

    public static final String COL_ARTICLE_TYPE = "article_type";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_COUNT_VIEW = "count_view";

    public static final String COL_COUNT_COMMENT = "count_comment";
}
