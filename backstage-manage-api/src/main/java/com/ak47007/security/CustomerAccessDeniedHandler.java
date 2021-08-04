package com.ak47007.security;

import com.ak47007.model.base.Result;
import com.ak47007.utils.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author AK47007
 * date 2019/12/9
 * 没有权限处理,登陆过的用户访问接口没有权限处理
 */
@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        ResponseUtil.response(Result.error("权限不足"), response);
    }
}
