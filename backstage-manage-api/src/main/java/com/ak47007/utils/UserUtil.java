package com.ak47007.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;
import com.ak47007.constant.RedisConstant;
import com.ak47007.model.SysUser;
import com.ak47007.redis.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author AK47007
 * CreateDate： 2019/11/19
 */
@Component
public class UserUtil {

    @Resource
    private RedisService redisService;


    /**
     * 将User对象存入Redis,设置过期时间为300分钟,并返回token
     *
     * @param user     user对象
     * @param remember 记住我
     * @return 返回Token
     */
    public String setUser(SysUser user, Boolean remember) {
        //生成token
        String token = SecureUtil.md5((UUID.randomUUID() + user.getUserName())).toUpperCase();
        Long time = RedisConstant.TOKEN_DEFAULT_TIME;
        if (remember) {
            time = RedisConstant.TOKEN_REMEMBER_TIME;
        }
        redisService.setObject(token, user, time);
        return token;
    }


    /**
     * 获得User对象
     *
     * @param request 请求对象
     * @return 返回User对象
     */
    public SysUser getUser(HttpServletRequest request) {
        String key = getTokenFromCookie(request);
        // 没有从COOKIE中获取到登录token
        if (key != null) {
            try {
                return redisService.getValue(key, SysUser.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从Cookie获取Token
     *
     * @param request 请求
     * @return token
     */
    public String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(RedisConstant.TOKEN_KEY_STR)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 退出登录
     *
     * @param request 请求对象
     */
    public void logout(HttpServletRequest request) {
        String token = getTokenFromCookie(request);
        if (token == null) {
            return;
        }
        redisService.delValue(token);
    }

}
