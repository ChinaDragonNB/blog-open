package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;

/**
 * 友情链接
 */
@Data
@TableName(value = "`links`")
public class Link {
    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 网站名称
     */
    @ApiModelProperty(value = "网站标题", required = true)
    @TableField(value = "`title`")
    private String title;

    /**
     * 网站链接
     */
    @ApiModelProperty(value = "网站链接", required = true)
    @TableField(value = "`link`")
    private String link;

    /**
     * logo链接
     */
    @ApiModelProperty(value = "网站logo链接", required = true)
    @TableField(value = "`logo`")
    private String logo;

    /**
     * 常用邮箱
     */
    @ApiModelProperty(value = "联系邮箱")
    @TableField(value = "`email`")
    private String email;

    /**
     * 状态：0、未通过 1、待审核、2、已通过
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`check_status`")
    private Integer checkStatus;

    /**
     * 通过时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`pass_time`")
    private LocalDateTime passTime;

    /**
     * 用户id
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`user_id`")
    private Long userId;

    public static final String COL_ID = "id";

    public static final String COL_TITLE = "title";

    public static final String COL_LINK = "link";

    public static final String COL_LOGO = "logo";

    public static final String COL_EMAIL = "email";

    public static final String COL_CHECK_STATUS = "check_status";

    public static final String COL_PASS_TIME = "pass_time";

    public static final String COL_USER_ID = "user_id";


    /**
     * SQL
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private String sql;
}