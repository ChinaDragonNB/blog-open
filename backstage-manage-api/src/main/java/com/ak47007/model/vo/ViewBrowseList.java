package com.ak47007.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author AK47007
 * @date 2020/7/11
 * Describe:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "view_browse_list")
public class ViewBrowseList implements Serializable {

    @ApiModelProperty(value = "文章标题")
    @TableField(value = "title")
    private String title;

    @ApiModelProperty(hidden = true)
    @TableField(value = "id")
    private Long id;

    @ApiModelProperty(value = "文章ID")
    @TableField(value = "article_id")
    private Long articleId;

    @ApiModelProperty(value = "浏览人IP")
    @TableField(value = "browse_ip")
    private String browseIp;

    @ApiModelProperty(value = "浏览时间")
    @TableField(value = "browse_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime browseTime;

    @ApiModelProperty(value = "浏览时间")
    @TableField(value = "browse_date")
    private LocalDate browseDate;

    @ApiModelProperty(value = "地理位置")
    @TableField(value = "ip_location")
    private String ipLocation;

    private static final long serialVersionUID = 1L;

    public static final String COL_TITLE = "title";

    public static final String COL_ID = "id";

    public static final String COL_ARTICLE_ID = "article_id";

    public static final String COL_BROWSE_IP = "browse_ip";

    public static final String COL_BROWSE_TIME = "browse_time";

    public static final String COL_BROWSE_DATE = "browse_date";

    public static final String COL_IP_LOCATION = "ip_location";
}