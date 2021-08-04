package com.ak47007.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author AK47007
 * @date 2020/6/7
 * Describe: 自定义属性读取
 */
@ConfigurationProperties(prefix = "custom")
@Component
@Data
public class ReadConfigUtil {

    /**
     * markdown文件路径前缀
     */
    private String markdownUrl;
}
