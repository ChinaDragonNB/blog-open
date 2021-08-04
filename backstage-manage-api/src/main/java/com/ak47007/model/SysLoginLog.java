package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;

/**
 * @author AK47007
 * @date 2020/6/5
 * Describe: 登录记录表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_login_log")
@Builder
public class SysLoginLog implements Serializable {
    /**
     * id主键
     */
    @ApiModelProperty(value = "id主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 登录人
     */
    @ApiModelProperty(value = "登录人ID")
    @TableField(value = "user_id")
    private Long userId;

    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    @TableField(value = "ip_addr")
    private String ipAddr;

    /**
     * 登录地址,根据ip获取
     */
    @ApiModelProperty(value = "登录地址")
    @TableField(value = "login_addr")
    private String loginAddr;

    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间")
    @TableField(value = "login_time")
    private LocalDateTime loginTime;


    @ApiModelProperty(value = "登陆人昵称")
    @Transient
    @TableField(exist = false)
    private String nickName;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_IP_ADDR = "ip_addr";

    public static final String COL_LOGIN_ADDR = "login_addr";

    public static final String COL_LOGIN_TIME = "login_time";
}