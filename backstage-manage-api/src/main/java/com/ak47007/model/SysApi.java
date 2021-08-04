package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;import javax.persistence.Transient;

@Data
@TableName(value = "`sys_api`")
public class SysApi {
    /**
     * 主键
     */
    @ApiModelProperty(value = "接口ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接口地址
     */
    @ApiModelProperty(value = "接口地址")
    @TableField(value = "`api_url`")
    private String apiUrl;

    /**
     * 接口描述
     */
    @ApiModelProperty(value = "接口描述")
    @TableField(value = "`description`")
    private String description;

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id")
    @TableField(value = "`parent_id`")
    private Long parentId;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`create_time`")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`create_user`")
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`update_time`")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`update_user`")
    private Long updateUser;

    /**
     * 排序
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`order`")
    private Integer order;

    public static final String COL_ID = "id";

    public static final String COL_API_URL = "api_url";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_ORDER = "order";


    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private List<SysApi> children;
}