package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文章浏览记录表
 */
@Data
@TableName(value = "`sys_browse_log`")
public class SysBrowseLog {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 访问的文章id
     */
    @ApiModelProperty(value = "访问的文章id")
    @TableField(value = "`article_id`")
    private Long articleId;

    /**
     * 访问者IP
     */
    @ApiModelProperty(value = "访问者IP")
    @TableField(value = "`browse_ip`")
    private String browseIp;

    /**
     * 访问时间
     */
    @ApiModelProperty(value = "访问时间")
    @TableField(value = "`browse_time`")
    private LocalDateTime browseTime;

    /**
     * ip所在位置
     */
    @ApiModelProperty(value = "地理位置")
    @TableField(value = "`ip_location`")
    private String ipLocation;

    public static final String COL_ID = "id";

    public static final String COL_ARTICLE_ID = "article_id";

    public static final String COL_BROWSE_IP = "browse_ip";

    public static final String COL_BROWSE_TIME = "browse_time";

    public static final String COL_IP_LOCATION = "ip_location";
}