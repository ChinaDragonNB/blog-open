package com.ak47007.model.addr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * @date 2020/6/20
 * Describe:
 */
@Data
public class AdInfo {

    /**
     * 国家
     */
    @ApiModelProperty(value = "国家")
    private String nation;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String district;

    /**
     * 行政区划代码
     */
    @ApiModelProperty(value = "行政区划代码")
    private Integer adCode;
}
