package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * date 2021/5/1 17:23
 * describes: 
 */
@Data
@TableName(value = "`sys_role_router`")
public class SysRoleRouter {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色Id
     */
    @ApiModelProperty(value = "角色Id")
    @TableField(value = "`role_id`")
    private Long roleId;

    /**
     * 路由id
     */
    @ApiModelProperty(value = "路由id")
    @TableField(value = "`router_id`")
    private Long routerId;

    public static final String COL_ID = "id";

    public static final String COL_ROLE_ID = "role_id";

    public static final String COL_ROUTER_ID = "router_id";
}