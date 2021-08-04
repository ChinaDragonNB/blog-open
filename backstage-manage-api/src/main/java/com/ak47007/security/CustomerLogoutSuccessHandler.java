package com.ak47007.security;

import com.ak47007.model.base.Result;
import com.ak47007.utils.ResponseUtil;
import com.ak47007.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author AK47007
 * @date 2019/12/8
 * Describe: 退出登录
 */
@Component
@Slf4j
public class CustomerLogoutSuccessHandler implements LogoutSuccessHandler {


    private final UserUtil userUtil;

    public CustomerLogoutSuccessHandler(UserUtil userUtil) {
        this.userUtil = userUtil;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        try {
            // 清除Redis中的缓存
            userUtil.logout(request);
            ResponseUtil.response(Result.success(), response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            ResponseUtil.response(Result.error(), response);
        }

    }
}
