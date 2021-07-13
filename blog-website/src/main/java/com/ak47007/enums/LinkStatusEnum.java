package com.ak47007.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ak47007
 * @date 2020/5/21
 * 描述：友情链接状态枚举
 */
@Getter
@AllArgsConstructor
public enum LinkStatusEnum {

    NOT_PASS(0, "未通过"),
    CHECKING(1, "审核中"),
    PASS(2, "已通过");


    /**
     * 状态值
     */
    private Integer value;

    /**
     * 状态描述
     */
    private String des;

}
