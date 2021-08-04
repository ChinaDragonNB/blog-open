package com.ak47007.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author AK47007
 * @date 2020/11/14 15:46
 * describe：Bucket 视图对象
 */
@Data
public class BucketVO {

    /**
     * bucket名称
     */
    @ApiModelProperty(value = "bucket名称")
    private String bucketName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 存储类型
     */
    @ApiModelProperty(value = "存储类型")
    private String storageType;

    /**
     * 数据中心
     */
    @ApiModelProperty(value = "数据中心")
    private String location;

    /**
     * 外网域名
     */
    @ApiModelProperty(value = "外网域名")
    private String extranetEndpoint;

    /**
     * 内网域名
     */
    @ApiModelProperty(value = "内网域名")
    private String intranetEndpoint;
}
