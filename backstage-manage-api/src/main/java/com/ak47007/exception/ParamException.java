package com.ak47007.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author AK47007
 * @date 2020/6/7
 * Describe: 参数异常
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ParamException extends RuntimeException {

    /**
     * 异常消息内容
     */
    private String ex;

}
