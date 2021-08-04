package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;

/**
 * @author AK47007
 * date 2021/5/4 17:09
 * describes:
 */

/**
 * 路由表,左侧菜单
 */
@Data
@TableName(value = "`sys_router`")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRouter {
    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 组件Name
     */
    @ApiModelProperty(value = "组件名称", required = true)
    @TableField(value = "`name`")
    private String name;

    /**
     * 组件标题
     */
    @ApiModelProperty(value = "菜单标题", required = true)
    @TableField(value = "`title`")
    private String title;

    /**
     * 路由路径
     */
    @ApiModelProperty(value = "菜单URI", required = true)
    @TableField(value = "`path`")
    private String path;

    /**
     * 组件路径
     */
    @ApiModelProperty(value = "组件路径", required = true)
    @TableField(value = "`component`")
    private String component;

    /**
     * 重定向路径
     */
    @ApiModelProperty(value = "重定向路由")
    @TableField(value = "`redirect`")
    private String redirect;

    /**
     * 图标名
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`icon`")
    private String icon;

    /**
     * 是否隐藏
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`hidden`")
    private Boolean hidden;

    /**
     * 父级路由,如果没有,则为0
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`parent_id`")
    private Long parentId;

    /**
     * 该路由是否不缓存
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`no_cache`")
    private Boolean noCache;

    /**
     * 该路由的描述
     */
    @ApiModelProperty(value = "路由描述")
    @TableField(value = "`describe`")
    private String describe;

    /**
     * 如果设置为true，将始终显示根菜单,如果不设置alwaysShow, 当项目有多个子路由时，它将成为嵌套模式，否则不显示根菜单
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`always_show`")
    private Boolean alwaysShow;

    /**
     * 如果设置路径，侧栏将突出显示您设置的路径,一般只有子级菜单隐藏的时候才会设置
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`active_menu`")
    private String activeMenu;

    /**
     * 路由显示顺序
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`order_index`")
    private Integer orderIndex;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`create_time`")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "`create_user`")
    private Long createUser;

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_TITLE = "title";

    public static final String COL_PATH = "path";

    public static final String COL_COMPONENT = "component";

    public static final String COL_REDIRECT = "redirect";

    public static final String COL_ICON = "icon";

    public static final String COL_HIDDEN = "hidden";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_NO_CACHE = "no_cache";

    public static final String COL_DESCRIBE = "describe";

    public static final String COL_ALWAYS_SHOW = "always_show";

    public static final String COL_ACTIVE_MENU = "active_menu";

    public static final String COL_ORDER_INDEX = "order_index";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_CREATE_USER = "create_user";
    /**
     * 分层级时用得到
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private List<SysRouter> children;
    /**
     * 路由meta属性
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @TableField(exist = false)
    private RouterMeta meta;
}