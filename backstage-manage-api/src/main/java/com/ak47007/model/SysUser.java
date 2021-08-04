package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;

/**
 * @author AK47007
 * date 2021/4/30 20:08
 * describes:
 */
@Data
@TableName(value = "sys_user")
public class SysUser {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 昵称,用于显示
     */
    @ApiModelProperty(value = "昵称", required = true)
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 用户名,用于登录
     */
    @ApiModelProperty(value = "用户名", required = true)
    @TableField(value = "user_name")
    private String userName;

    /**
     * 密码,用于登录
     */
    @ApiModelProperty(value = "密码", required = true)
    @TableField(value = "user_pass")
    private String userPass;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "头像")
    @TableField(value = "login_img")
    private String loginImg;

    /**
     * 拥有角色
     */
    @ApiModelProperty(value = "拥有角色ID", required = true)
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 状态 0锁定 1正常
     */
    @ApiModelProperty(value = "状态", required = true)
    @TableField(value = "`state`")
    private Boolean state;

    /**
     * 用户创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 博客主页
     */
    @ApiModelProperty(value = "博客主页", required = true)
    @TableField(value = "blog_home")
    private String blogHome;


    /*  自定义属性  */

    /**
     * SQL
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private String sql;

    /**
     * 角色名称
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private String roleName;

    /**
     * 所属角色
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private SysRole role;

    public static final String COL_ID = "id";

    public static final String COL_NICK_NAME = "nick_name";

    public static final String COL_USER_NAME = "user_name";

    public static final String COL_USER_PASS = "user_pass";

    public static final String COL_LOGIN_IMG = "login_img";

    public static final String COL_ROLE_ID = "role_id";

    public static final String COL_STATE = "state";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_BLOG_HOME = "blog_home";
}