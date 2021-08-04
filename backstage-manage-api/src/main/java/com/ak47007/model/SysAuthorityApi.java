package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * date 2021/5/1 17:03
 * describes: 
 */
@Data
@TableName(value = "sys_authority_api")
public class SysAuthorityApi {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限id
     */
    @ApiModelProperty(value = "权限id")
    @TableField(value = "authority_id")
    private Long authorityId;

    /**
     * 接口路径
     */
    @ApiModelProperty(value = "接口ID")
    @TableField(value = "api_id")
    private Long apiId;

    public static final String COL_ID = "id";

    public static final String COL_AUTHORITY_ID = "authority_id";

    public static final String COL_API_ID = "api_id";
}