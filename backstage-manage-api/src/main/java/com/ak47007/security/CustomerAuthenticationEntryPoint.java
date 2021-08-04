package com.ak47007.security;

import com.ak47007.enums.ResultEnum;
import com.ak47007.utils.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * /**
 *
 * @author AK47007
 * date 2019/12/9
 * Describe: 未登录用户无权限访问配置类
 */
@Component
public class CustomerAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        ResultEnum notLogin = ResultEnum.NOT_LOGIN;
        ResponseUtil.response(notLogin.getResultCode(), notLogin.getResultMessage(), response);
    }
}
