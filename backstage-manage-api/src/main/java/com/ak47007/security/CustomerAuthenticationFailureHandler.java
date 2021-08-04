package com.ak47007.security;

import com.ak47007.model.base.Result;
import com.ak47007.utils.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author AK47007
 * date 2019/12/8
 * Describe: 登录失败处理器
 */
@Component
public class CustomerAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        ResponseUtil.response(Result.error(e.getMessage()), response);
    }
}
