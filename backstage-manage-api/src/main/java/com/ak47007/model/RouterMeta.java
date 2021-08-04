package com.ak47007.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * @date 2019/12/10
 * Describe: 路由的meta属性
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouterMeta {


    /**
     * 菜单标题
     */
    @ApiModelProperty(value = "菜单标题")
    private String title;


    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String icon;

    /**
     * 菜单是否不缓存
     */
    @ApiModelProperty(value = "菜单是否不缓存")
    private Boolean noCache;

    /**
     * 如果设置路径，侧栏将突出显示您设置的路径,一般只有子级菜单隐藏的时候才会设置
     */
    @ApiModelProperty(value = "如果设置路径，侧栏将突出显示您设置的路径,一般只有子级菜单隐藏的时候才会设置")
    private String activeMenu;

}
