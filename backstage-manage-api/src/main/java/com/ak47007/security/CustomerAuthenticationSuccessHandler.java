package com.ak47007.security;

import com.ak47007.model.base.Result;
import com.ak47007.service.UserService;
import com.ak47007.utils.LocationUtil;
import com.ak47007.utils.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author AK47007
 * @date 2019/12/8
 * Describe: 登录成功处理
 */
@Component
@AllArgsConstructor
public class CustomerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 用户名
        String userName = (String) authentication.getPrincipal();
        // 获取登录者IP
        String loginIp = LocationUtil.getIpAddr(request);
        Boolean remember = Boolean.valueOf(request.getParameter("remember"));
        Result<?> result = userService.login(userName, loginIp, remember);
        ResponseUtil.response(result, response);
    }
}
