package com.ak47007.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthorityApiVO {
    /**
    * 主键
    */
    @ApiModelProperty(value = "主键")
    private Long authorityApiId;

    /**
    * 权限id
    */
    @ApiModelProperty(value = "权限id")
    private Long authorityId;

    /**
    * 接口路径
    */
    @ApiModelProperty(value = "接口路径")
    private Long apiId;


    /**
     * 接口地址
     */
    @ApiModelProperty(value = "接口地址")
    private String apiUrl;

    /**
     * 接口描述
     */
    @ApiModelProperty(value = "接口描述")
    private String description;


    /**
     * 权限英文名
     */
    @ApiModelProperty(value = "权限英文名")
    private String authorityNameEn;

    /**
     * 权限中文名
     */
    @ApiModelProperty(value = "权限中文名")
    private String authorityNameCn;

    /**
     * 父级权限,最高级时为0
     */
    @ApiModelProperty(value = "父级权限")
    private Long parentId;

    /**
     * 权限创建时间
     */
    @ApiModelProperty(hidden = true)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 权限创建人
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 状态 0锁定 1正常
     */
    @ApiModelProperty(value = "状态 0锁定 1正常")
    private Boolean state;
}