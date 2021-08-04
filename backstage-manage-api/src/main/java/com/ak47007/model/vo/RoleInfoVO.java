package com.ak47007.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author AK47007
 * date 2021/5/3 19:56
 * describes:
 */
@Data
public class RoleInfoVO {

    @ApiModelProperty(value = "角色英文名")
    private String roleNameEn;

    @ApiModelProperty(value = "角色中文名")
    private String roleNameCn;

    @ApiModelProperty(value = "状态")
    private Boolean state;

    /**
     * 角色拥有的权限
     */
    @ApiModelProperty(value = "角色拥有的权限")
    private List<Long> authorityDefaultChecked;

    /**
     * 角色拥有的模块
     */
    @ApiModelProperty(value = "角色拥有的模块")
    private List<Long> routerDefaultChecked;
}
