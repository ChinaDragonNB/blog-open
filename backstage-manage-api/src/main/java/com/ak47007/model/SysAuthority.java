package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;

/**
 * 权限
 */
@Data
@TableName(value = "`sys_authority`")
public class SysAuthority {
    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限英文名
     */
    @ApiModelProperty(value = "权限英文名", required = true)
    @TableField(value = "`authority_name_en`")
    private String authorityNameEn;

    /**
     * 权限中文名
     */
    @ApiModelProperty(value = "权限描述", required = true)
    @TableField(value = "`authority_name_cn`")
    private String authorityNameCn;

    /**
     * 父级权限,最高级时为0
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`parent_id`")
    private Long parentId;

    /**
     * 权限创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`create_time`")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 权限创建人
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`create_user`")
    private Long createUser;

    /**
     * 状态 0锁定 1正常
     */
    @ApiModelProperty(value = "状态 0锁定 1正常", required = true)
    @TableField(value = "`state`")
    private Boolean state;

    /**
     * 排序
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`order`")
    private Integer order;

    /**
     * 是否为公共权限,每个角色都有的
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`is_common`")
    private Boolean isCommon;

    public static final String COL_ID = "id";

    public static final String COL_AUTHORITY_NAME_EN = "authority_name_en";

    public static final String COL_AUTHORITY_NAME_CN = "authority_name_cn";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_STATE = "state";

    public static final String COL_ORDER = "order";

    public static final String COL_IS_COMMON = "is_common";

    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private List<Long> apiIds;
}