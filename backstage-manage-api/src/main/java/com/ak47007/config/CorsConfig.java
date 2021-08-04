package com.ak47007.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author AK47007
 * @date 2020/3/15
 * Describe: 跨域配置
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 页面跨域访问Controller过滤
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        // 设置允许跨域的路径
        CorsRegistration corsRegistration = registry.addMapping("/**");
        // 设置允许跨域请求的域名
        corsRegistration.allowedOrigins("*");
        // 设置允许的header属性
        corsRegistration.allowedHeaders("*");
        // 设置允许的方法
        corsRegistration.allowedMethods("POST","GET","OPTION");
        // 允许跨域Cookie
        corsRegistration.allowCredentials(true);
    }
}
