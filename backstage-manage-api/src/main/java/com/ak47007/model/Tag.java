package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;

/**
 * 标签表
 */
@Data
@TableName(value = "`tags`")
public class Tag {
    private static final long serialVersionUID = 1L;


    /**
     * 标签ID
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "tag_id", type = IdType.AUTO)
    private Long tagId;

    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称", required = true)
    @TableField(value = "`tag_name`")
    private String tagName;

    /**
     * 标签logo
     */
    @ApiModelProperty(value = "标签logo", required = true)
    @TableField(value = "`tag_logo`")
    private String tagLogo;

    /**
     * 标签类型:1.单个 2.多个
     */
    @ApiModelProperty(value = "标签类型 1:单个 2:组合", required = true)
    @TableField(value = "`tag_type`")
    private Integer tagType;

    /**
     * 标签创建人
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`user_id`")
    private Long userId;

    public static final String COL_TAG_ID = "tag_id";

    public static final String COL_TAG_NAME = "tag_name";

    public static final String COL_TAG_LOGO = "tag_logo";

    public static final String COL_TAG_TYPE = "tag_type";

    public static final String COL_USER_ID = "user_id";

    /**
     * 自定义SQL
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private String sql;
}