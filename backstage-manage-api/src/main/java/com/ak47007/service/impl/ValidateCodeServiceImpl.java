package com.ak47007.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.ak47007.constant.RedisConstant;
import com.ak47007.model.base.Result;
import com.ak47007.redis.RedisService;
import com.ak47007.service.ValidateCodeService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@AllArgsConstructor
public class ValidateCodeServiceImpl implements ValidateCodeService {

    private final RedisService redisService;

    @Override
    public void create(String key, HttpServletResponse response) throws Exception {
        if (Strings.isBlank(key)) {
            throw new Exception("验证码key不能为空");
        }
        response.setContentType("image/png");
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // 自定义纯数字的验证码
            LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(115, 42, 4, 100);
            lineCaptcha.write(outputStream);
            redisService.setObject(key, lineCaptcha.getCode(), RedisConstant.CAPTCHA_SAVE_TIME);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

    }

    @Override
    public Result check(String key, String value) {
        if (Strings.isBlank(value)) {
            return Result.error("请输入验证码");
        }
        String code = redisService.getValue(key, String.class);
        redisService.delValue(key);
        if (Strings.isBlank(code)) {
            return Result.error("验证码已过期");
        }
        if (!value.equals(code)) {
            return Result.error("验证码不正确");
        }

        return Result.success();
    }

}
