package com.ak47007.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName(value = "`view_article_list`")
public class ViewArticleList {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "文章ID")
    @TableField(value = "`id`")
    private Long id;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    @TableField(value = "`title`")
    private String title;

    /**
     * 文章描述
     */
    @ApiModelProperty(value = "文章描述")
    @TableField(value = "`article_describe`")
    private String articleDescribe;

    /**
     * 访问数量
     */
    @ApiModelProperty(value = "访问数量")
    @TableField(value = "`views`")
    private Long views;

    /**
     * 标签logo
     */
    @ApiModelProperty(value = "标签logo")
    @TableField(value = "`tag_logo`")
    private String tagLogo;

    /**
     * 发布状态：1.public  2.private
     */
    @ApiModelProperty(value = "发布状态")
    @TableField(value = "`state`")
    private Integer state;

    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    @TableField(value = "`publish_time`")
    private LocalDateTime publishTime;

    /**
     * 文章作者
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`user_id`")
    private Long userId;

    public static final String COL_ID = "id";

    public static final String COL_TITLE = "title";

    public static final String COL_ARTICLE_DESCRIBE = "article_describe";

    public static final String COL_VIEWS = "views";

    public static final String COL_TAG_LOGO = "tag_logo";

    public static final String COL_STATE = "state";

    public static final String COL_PUBLISH_TIME = "publish_time";

    public static final String COL_USER_ID = "user_id";
}