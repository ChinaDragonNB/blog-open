package com.ak47007.model.vo;

import com.ak47007.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AK47007
 * @date 2020/5/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private Integer resultCode;

    private String resultMessage;

    private Object resultData;


    public Result(Integer resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    /**
     * 成功
     *
     * @return
     */
    public static Result success() {
        return new Result(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 成功,并且附带一个对象
     *
     * @return
     */
    public static Result success(Object data) {
        return new Result(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMessage(),data);
    }

    /**
     * 失败
     *
     * @return
     */
    public static Result error() {
        return new Result(ResultEnum.ERROR.getCode(),ResultEnum.ERROR.getMessage());
    }

    /**
     * 失败,并且附带一个对象
     *
     * @return
     */
    public static Result error(Object data) {
        return new Result(ResultEnum.ERROR.getCode(),ResultEnum.ERROR.getMessage(),data);
    }
}
