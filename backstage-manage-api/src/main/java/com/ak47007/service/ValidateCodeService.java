package com.ak47007.service;

import com.ak47007.model.base.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ValidateCodeService {

    /**
     * 生成验证码
     *
     * @param key      验证码 uuid
     * @param response HttpServletResponse
     * @throws IOException 异常
     */
    void create(String key, HttpServletResponse response) throws Exception;

    /**
     * 校验验证码
     *
     * @param key   前端上送 key
     * @param value 前端上送待校验值
     */
    Result check(String key, String value);
}
