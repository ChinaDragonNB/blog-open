package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * date 2021/5/1 17:17
 * describes: 
 */
/**
    * 角色权限表
    */
@Data
@TableName(value = "`sys_role_authority`")
public class SysRoleAuthority {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    @TableField(value = "`role_id`")
    private Long roleId;

    /**
     * 父级权限,最高级时为0
     */
    @ApiModelProperty(value = "父级权限")
    @TableField(value = "`authority_id`")
    private Long authorityId;

    public static final String COL_ID = "id";

    public static final String COL_ROLE_ID = "role_id";

    public static final String COL_AUTHORITY_ID = "authority_id";
}