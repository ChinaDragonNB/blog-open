package com.ak47007.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LinkStatusEnum {


    PASS(2, "通过"),
    NOT_PASS(0, "未通过"),
    CHECKING(1, "待审核");

    private Integer status;
    private String dec;
}
