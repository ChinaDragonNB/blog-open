package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;

/**
 * @author AK47007
 * date 2021/5/1 17:12
 * describes: 角色表
 */
@Data
@TableName(value = "`sys_role`")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SysRole {
    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色英文名
     */
    @ApiModelProperty(value = "角色英文名", required = true)
    @TableField(value = "`role_name_en`")
    private String roleNameEn;

    /**
     * 角色中文名
     */
    @ApiModelProperty(value = "角色中文名", required = true)
    @TableField(value = "`role_name_cn`")
    private String roleNameCn;

    /**
     * 角色创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`create_time`")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 角色创建人
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`create_user`")
    private Long createUser;

    /**
     * 状态 0锁定 1正常
     */
    @ApiModelProperty(value = "状态 0:锁定,1:正常", required = true)
    @TableField(value = "`state`")
    private Boolean state;

    /**
     * SQL
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private String sql;

    /**
     * 拥有权限
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private List<SysAuthority> authorityList;


    /**
     * 拥有模块
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private List<SysRouter> routerList;

    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private List<Long> routerIds;


    public static final String COL_ID = "id";

    public static final String COL_ROLE_NAME_EN = "role_name_en";

    public static final String COL_ROLE_NAME_CN = "role_name_cn";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_STATE = "state";
}