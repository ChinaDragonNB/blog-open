package com.ak47007.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author AK47007
 * @date 2020/5/18
 */
@AllArgsConstructor
@Getter
public enum ResultEnum {

    /**
     * 成功
     */
    SUCCESS(1,"成功"),
    /**
     * 失败
     */
    ERROR(0,"失败");


    private Integer code;

    private String message;
}
