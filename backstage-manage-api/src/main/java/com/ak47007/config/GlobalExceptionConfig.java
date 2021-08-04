package com.ak47007.config;

import com.ak47007.model.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author AK47007
 * @date 2019/12/5
 * 全局异常
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionConfig {


    @ResponseBody
    @ExceptionHandler(Exception.class)
    public <T> Result<T> handleException(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error("服务器异常，请稍后重试");
    }


}
