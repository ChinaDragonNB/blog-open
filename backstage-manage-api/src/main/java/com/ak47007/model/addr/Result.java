package com.ak47007.model.addr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * @date 2020/6/20
 * Describe: 获取ip地址的结果
 */
@Data
public class Result {

    /**
     * 用于定位的IP地址
     */
    @ApiModelProperty(value = "用于定位的IP地址")
    private String ip;

    /**
     * 定位坐标
     */
    @ApiModelProperty(value = "定位坐标")
    private Location location;

    /**
     * 定位行政区划信息
     */
    @ApiModelProperty(value = "定位行政区划信息")
    private AdInfo adInfo;

}
