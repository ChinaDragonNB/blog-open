package com.ak47007.model.dto;

import com.ak47007.model.SysRole;
import com.ak47007.model.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author AK47007
 * date 2021/5/4 15:08
 * describes:
 */
@Data
public class RoleDTO {

    private SysRole role;

    @ApiModelProperty(hidden = true)
    private SysUser user;

    /**
     * 是否操作过权限
     */
    @ApiModelProperty(value = "是否重新选择过权限", required = true)
    private Boolean checkFlag1;

    /**
     * 是否操作过路由
     */
    @ApiModelProperty(value = "是否重新选择过路由(模块菜单)", required = true)
    private Boolean checkFlag2;

    /**
     * 选中的权限ID
     */
    @ApiModelProperty(value = "选择的权限ID集合", required = true)
    List<Long> authorityIds;

    /**
     * 选中的模块ID
     */
    @ApiModelProperty(value = "选择的路由(模块菜单)ID集合", required = true)
    List<Long> routerIds;
}
