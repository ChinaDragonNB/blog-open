package com.ak47007.model.addr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * @date 2020/6/20
 * Describe: 定位坐标
 */
@Data
public class Location {

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Integer lat;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Integer lng;

}
