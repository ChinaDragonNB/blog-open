package com.ak47007.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author AK47007
 * @date 2020/11/14 21:10
 * describe： 存储类型
 */
@AllArgsConstructor
@Getter
public enum StorageClassEnum {

    STANDARD("Standard", "标准存储"),
    INFREQUENT_ACCESS_1("IA", "低频访问存储"),
    INFREQUENT_ACCESS_2("Infrequent Access", "低频访问存储"),
    ARCHIVE("Archive", "归档存储"),
    COLD_ARCHIVE("Cold Archive", "冷归档存储");


    /**
     * 英文
     */
    private final String en;

    /**
     * 中文
     */
    private final String cn;
}
