package com.ak47007.constant;

/**
 * Redis使用常量
 */
public interface RedisConstant {

    /**
     * 默认保存时间,单位为分钟
     */
    Long TOKEN_DEFAULT_TIME = 90L;

    /**
     * 勾选了,记住我保存时间
     */
    Long TOKEN_REMEMBER_TIME = 10080L;

    String TOKEN_KEY_STR = "user_token";

    /**
     * 最近文章保存时间
     */
    Long ARTICLE_SAVE_TIME = 10080L;

    /**
     * 验证码保存时间
     */
    Long CAPTCHA_SAVE_TIME = 1L;
}
