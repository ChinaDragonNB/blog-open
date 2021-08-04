package com.ak47007.config;

import com.ak47007.exception.AssertException;
import com.ak47007.model.base.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author AK47007
 * @date 2020/7/31 19:18
 * describe：全局捕捉断言异常
 */
@ControllerAdvice
public class AssertExceptionConfig {

    @ResponseBody
    @ExceptionHandler(AssertException.class)
    public <T> Result<T> handleException(Exception e) {
        return Result.error(e.getMessage());
    }
}
