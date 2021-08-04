package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;import javax.persistence.Transient;

/**
 * 操作日志
 */
@Data
@TableName(value = "`sys_oper_log`")
public class SysOperLog {
    public static final String COL_OPER_MODUL = "oper_modul";
    public static final String COL_ERROR_STATUS = "error_status";
    public static final String COL_STATE = "state";
    public static final String COL_ERROR_MSG = "error_msg";
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作模块
     */
    @ApiModelProperty(value = "操作模块")
    @TableField(value = "`oper_module`")
    private String operModule;

    /**
     * 操作类型,与字典表项的值对应
     */
    @ApiModelProperty(value = "操作类型")
    @TableField(value = "`oper_type`")
    private Integer operType;

    /**
     * 操作描述
     */
    @ApiModelProperty(value = "操作描述")
    @TableField(value = "`oper_desc`")
    private String operDesc;

    /**
     * 请求url
     */
    @ApiModelProperty(value = "请求url")
    @TableField(value = "`oper_url`")
    private String operUrl;

    /**
     * 操作方法
     */
    @ApiModelProperty(value = "请求方法")
    @TableField(value = "`oper_method`")
    private String operMethod;

    /**
     * 请求方式
     */
    @ApiModelProperty(value = "请求方式")
    @TableField(value = "`request_method`")
    private String requestMethod;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    @TableField(value = "`oper_req_param`")
    private String operReqParam;

    /**
     * 返回参数
     */
    @ApiModelProperty(value = "返回参数")
    @TableField(value = "`oper_res_param`")
    private String operResParam;

    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    @TableField(value = "`oper_user_id`")
    private Long operUserId;

    /**
     * 操作人昵称
     */
    @ApiModelProperty(value = "操作人昵称")
    @TableField(value = "`oper_nick_name`")
    private String operNickName;

    /**
     * 主机ip
     */
    @ApiModelProperty(value = "操作人IP")
    @TableField(value = "`oper_ip`")
    private String operIp;

    /**
     * 操作地点
     */
    @ApiModelProperty(value = "操作人地理位置")
    @TableField(value = "`oper_location`")
    private String operLocation;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    @TableField(value = "`oper_time`")
    private LocalDateTime operTime;

    public static final String COL_ID = "id";

    public static final String COL_OPER_MODULE = "oper_module";

    public static final String COL_OPER_TYPE = "oper_type";

    public static final String COL_OPER_DESC = "oper_desc";

    public static final String COL_OPER_URL = "oper_url";

    public static final String COL_OPER_METHOD = "oper_method";

    public static final String COL_REQUEST_METHOD = "request_method";

    public static final String COL_OPER_REQ_PARAM = "oper_req_param";

    public static final String COL_OPER_RES_PARAM = "oper_res_param";

    public static final String COL_OPER_USER_ID = "oper_user_id";

    public static final String COL_OPER_NICK_NAME = "oper_nick_name";

    public static final String COL_OPER_IP = "oper_ip";

    public static final String COL_OPER_LOCATION = "oper_location";

    public static final String COL_OPER_TIME = "oper_time";

    /**
     * 操作类型名称
     */
    @ApiModelProperty(value = "操作类型名称")
    @TableField(exist = false)
    @Transient
    private String operTypeName;
}