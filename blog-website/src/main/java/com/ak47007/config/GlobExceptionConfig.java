package com.ak47007.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author AK47007
 * @date 2020/5/20
 */
@ControllerAdvice
@Slf4j
public class GlobExceptionConfig {

    @ExceptionHandler(value = Exception.class)
    public String exception( Exception ex) {
        ex.printStackTrace();
        log.info(ex.getMessage());
        return "error/500";
    }
}
