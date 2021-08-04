package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * @date 2020/11/12 21:50
 * describe：实用网站
 */
@Data
@TableName(value = "website_tools")
public class WebsiteTools {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer id;

    /**
     * 网站名称
     */
    @ApiModelProperty(value = "网站名称")
    @TableField(value = "website_name")
    private String websiteName;

    /**
     * 网站链接
     */
    @ApiModelProperty(value = "网站链接")
    @TableField(value = "website_url")
    private String websiteUrl;

    /**
     * 添加网站时间
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @TableField(value = "add_date_time")
    private LocalDateTime addDateTime;

    /**
     * 网站描述
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @TableField(value = "`desc`")
    private String desc;

    public static final String COL_ID = "id";

    public static final String COL_WEBSITE_NAME = "website_name";

    public static final String COL_WEBSITE_URL = "website_url";

    public static final String COL_ADD_DATE_TIME = "add_date_time";

    public static final String COL_DESC = "desc";
}