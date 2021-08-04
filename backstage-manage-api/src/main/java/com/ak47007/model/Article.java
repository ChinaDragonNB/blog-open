package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Transient;

/**
 * @author ak47007
 * @date 2020/5/23
 * 描述： 文章表
 */

@Data
@TableName(value = "`article`")
public class Article {

    private static final long serialVersionUID = 1L;
    /**
     * 编号
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * guid
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`uuid`")
    private String uuid;

    /**
     * 标题
     */
    @ApiModelProperty(value = "文章标题", required = true)
    @TableField(value = "`title`")
    private String title;

    /**
     * 封面图片
     */
    @ApiModelProperty(value = "封面图片", required = true)
    @TableField(value = "`cover`")
    private String cover;

    /**
     * 文章描述
     */
    @ApiModelProperty(value = "文章描述", required = true)
    @TableField(value = "`article_describe`")
    private String articleDescribe;

    /**
     * 发布时间
     */
    @ApiModelProperty(hidden = true)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "`publish_time`")
    private LocalDateTime publishTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "`edit_time`")
    private LocalDateTime editTime;

    /**
     * 标签
     */
    @ApiModelProperty(value = "文章所属分类", required = true)
    @TableField(value = "`tag_id`")
    private Long tagId;

    /**
     * 发布状态：1.public  2.private
     */
    @ApiModelProperty(value = "发布状态：1.public  2.private", required = true)
    @TableField(value = "`state`")
    private Integer state;

    /**
     * 是否置顶 1:是 0 否
     */
    @ApiModelProperty(value = "是否置顶 1:是 0 否", required = true)
    @TableField(value = "`is_stickie`")
    private Integer isStickie;

    /**
     * 文章类型,1 原创 2 转载 3 翻译
     */
    @ApiModelProperty(value = "文章类型,1 原创 2 转载 3 翻译", required = true)
    @TableField(value = "`article_type`")
    private Integer articleType;

    /**
     * 文章作者
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`user_id`")
    private Long userId;

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
    /**
     * 标签名
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private String tagName;
    /**
     * 标签Logo
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private String tagLogo;
    /**
     * 发布时间 Start
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(exist = false)
    private LocalDate publishTimeStart;
    /**
     * 发布时间 End
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(exist = false)
    private LocalDate publishTimeEnd;

    /**
     * 拼接SQL
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private String sql;

    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private Integer views;

    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private String markdown;

    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private String describe;
}