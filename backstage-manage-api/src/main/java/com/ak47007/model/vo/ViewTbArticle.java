package com.ak47007.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *@author ak47007
 *@date 2020/5/23
 *描述： 
 */   
@Data
@TableName(value = "view_tb_article")
public class ViewTbArticle {

    @ApiModelProperty(value = "")
    @TableField(value = "id")
    private Long id;

    @ApiModelProperty(value = "")
    @TableField(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "")
    @TableField(value = "cover")
    private String cover;

    @ApiModelProperty(value = "")
    @TableField(value = "title")
    private String title;

    @ApiModelProperty(value = "")
    @TableField(value = "article_describe")
    private String articleDescribe;

    @ApiModelProperty(value = "")
    @TableField(value = "publish_time")
    private Date publishTime;

    @ApiModelProperty(value = "")
    @TableField(value = "edit_time")
    private Date editTime;

    @ApiModelProperty(value = "")
    @TableField(value = "tag_id")
    private Long tagId;

    @ApiModelProperty(value = "")
    @TableField(value = "state")
    private Integer state;

    @ApiModelProperty(value = "是否置顶")
    @TableField(value = "is_stickie")
    private Integer isStickie;

    @ApiModelProperty(value = "文章类型")
    @TableField(value = "article_type")
    private Integer articleType;

    @ApiModelProperty(value = "作者ID")
    @TableField(value = "user_id")
    private Long userId;

    @ApiModelProperty(value = "文章内容")
    @TableField(value = "article_content")
    private String articleContent;

    @ApiModelProperty(value = "访问数量")
    @TableField(value = "count_view")
    private Long countView;

    @ApiModelProperty(value = "评论数量")
    @TableField(value = "count_comment")
    private Long countComment;

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
}