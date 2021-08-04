package com.ak47007.model.base;

import com.ak47007.enums.ResultEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author AK47007
 * @date 2019/8/31
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class Result<T> implements Serializable {

    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private Integer resultCode;

    /**
     * 消息
     */
    @ApiModelProperty(value = "消息描述")
    private String resultMessage;

    /**
     * 返回对象
     */
    @ApiModelProperty(value = "返回对象")
    private T resultData;

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.setResultCode(ResultEnum.SUCCESS.getResultCode());
        result.setResultMessage(ResultEnum.SUCCESS.getResultMessage());
        return result;
    }

    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<>();
        result.setResultCode(ResultEnum.SUCCESS.getResultCode());
        result.setResultMessage(message);
        return result;
    }


    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setResultCode(ResultEnum.SUCCESS.getResultCode());
        result.setResultMessage(ResultEnum.SUCCESS.getResultMessage());
        result.setResultData(data);
        return result;
    }

    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setResultCode(ResultEnum.SUCCESS.getResultCode());
        result.setResultMessage(message);
        result.setResultData(data);
        return result;
    }


    public static <T> Result<T> error() {
        Result<T> result = new Result<>();
        result.setResultCode(ResultEnum.ERROR.getResultCode());
        result.setResultMessage(ResultEnum.ERROR.getResultMessage());
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setResultCode(ResultEnum.ERROR.getResultCode());
        result.setResultMessage(message);
        return result;
    }


    public static <T> Result<T> error(T data) {
        Result<T> result = new Result<>();
        result.setResultCode(ResultEnum.ERROR.getResultCode());
        result.setResultMessage(ResultEnum.ERROR.getResultMessage());
        result.setResultData(data);
        return result;
    }

    public static <T> Result<T> error(String message, T data) {
        Result<T> result = new Result<>();
        result.setResultCode(ResultEnum.ERROR.getResultCode());
        result.setResultMessage(message);
        result.setResultData(data);
        return result;
    }
}
