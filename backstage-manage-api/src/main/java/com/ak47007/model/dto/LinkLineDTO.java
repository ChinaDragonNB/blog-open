package com.ak47007.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author AK47007
 * date 2021/4/24 21:22
 * describes:
 */
@Data
@AllArgsConstructor
public class LinkLineDTO {

    @ApiModelProperty(value = "1月")
    private Integer january;

    @ApiModelProperty(value = "2月")
    private Integer february;

    @ApiModelProperty(value = "3月")
    private Integer march;

    @ApiModelProperty(value = "4月")
    private Integer april;

    @ApiModelProperty(value = "5月")
    private Integer may;

    @ApiModelProperty(value = "6月")
    private Integer june;

    @ApiModelProperty(value = "7月")
    private Integer july;

    @ApiModelProperty(value = "8月")
    private Integer august;

    @ApiModelProperty(value = "9月")
    private Integer september;

    @ApiModelProperty(value = "10月")
    private Integer october;

    @ApiModelProperty(value = "11月")
    private Integer november;

    @ApiModelProperty(value = "12月")
    private Integer december;
}
