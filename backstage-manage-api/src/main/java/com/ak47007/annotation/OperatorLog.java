package com.ak47007.annotation;


import com.ak47007.enums.OperatorTypeEnum;

import java.lang.annotation.*;

/**
 * @author AK47007
 * date 2021/5/19 10:48
 * describes: 操作日志注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperatorLog {

    /**
     * 操作模块
     */
    String operModule() default "";

    /**
     * 操作类型
     */
    OperatorTypeEnum operType() default OperatorTypeEnum.OTHER;

    /**
     * 操作说明
     */
    String operDesc() default "";

}
