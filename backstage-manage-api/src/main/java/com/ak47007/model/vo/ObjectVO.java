package com.ak47007.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author AK47007
 * @date 2020/11/14 22:30
 * describe： 文件对象(包括文件夹)
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ObjectVO {

    /**
     * 对象(文件/文件夹)名称
     */
    @ApiModelProperty(value = "对象(文件/文件夹)名称")
    private String name;

    /**
     * 路径
     */
    @ApiModelProperty(value = "路径")
    private String path;

    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "最后更新时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
    private Date lastModified;

    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小")
    private Long size;

    /**
     * 存储类型
     */
    @ApiModelProperty(value = "存储类型")
    private String storageType;

    /**
     * 是否为文件夹
     */
    @ApiModelProperty(value = "是否为文件夹")
    private Boolean isDir;

    /**
     * 格式化后的文件大小
     */
    @ApiModelProperty(value = "格式化后的文件大小")
    private String formattedSize;

    /**
     * url
     */
    @ApiModelProperty(value = "url")
    private String url;

    /**
     * 图标类型
     */
    @ApiModelProperty(value = "图标类型")
    private String iconType;


}
