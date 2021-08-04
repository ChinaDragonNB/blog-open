package com.ak47007.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author AK47007
 * @date 2020/11/14 16:00
 * describe： 结果枚举类
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {

    SUCCESS(1, "success"),
    ERROR(-1, "error"),
    NOT_LOGIN(401, "身份验证会话已过期，请重新登录");


    private final Integer resultCode;

    private final String resultMessage;
}
