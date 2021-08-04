package com.ak47007.exception;

/**
 * @author AK47007
 * @date 2020/7/31 19:16
 * describe： 自定义断言异常
 */

public class AssertException extends IllegalArgumentException {

    public AssertException(String message) {
        super(message);
    }

}
