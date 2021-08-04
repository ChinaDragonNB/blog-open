package com.ak47007.model.addr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * @date 2020/6/20
 * Describe: 获取地址响应信息
 */
@Data
public class Response {

    /**
     * 状态码，0为正常,
     * 310请求参数信息有误，
     * 311Key格式错误,
     * 306请求有护持信息请检查字符串,
     * 110请求来源未被授权
     */
    @ApiModelProperty(value = "状态码")
    private Integer status;

    /**
     * 对status的描述
     */
    @ApiModelProperty(value = "对status的描述")
    private String message;

    /**
     * IP定位结果
     */
    @ApiModelProperty(value = "IP定位结果")
    private Result result;


}
