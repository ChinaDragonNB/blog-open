package com.ak47007.security;

import cn.hutool.crypto.SecureUtil;
import com.ak47007.service.impl.UserServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Collection;


/**
 * @author AK47007
 * @date 2019/12/8
 * Describe: 自定义登录处理
 */
@Component
public class CustomerAuthenticationProvider implements AuthenticationProvider {


    /**
     * 覆盖 loadUserByUsername 的类
     */
    @Resource
    private UserServiceImpl userService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        //表单输入的用户名
        String userName = (String) authentication.getPrincipal();
        //表单输入的密码
        String userPass = (String) authentication.getCredentials();
        UserDetails userDetails = userService.loadUserByUsername(userName);
        if (!userDetails.isEnabled()) {
            throw new DisabledException("该账户已锁定!");
        }
        String password = userDetails.getPassword();
        // 对加密密码进行验证
        if (passwordContrast(userPass, password)) {
            // 角色与权限
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, userPass, authorities);
            // 保存用户信息
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return authenticationToken;
        } else {
            throw new BadCredentialsException("用户名或密码错误");
        }
    }


    /**
     * 密码对比
     *
     * @param userPass    用户输入的密码
     * @param md5UserPass 数据库存储的密码
     * @return 密码比较
     */
    private boolean passwordContrast(String userPass, String md5UserPass) {
        if (userPass != null && md5UserPass != null) {
            try {
                return SecureUtil.md5(userPass).toUpperCase().equalsIgnoreCase(md5UserPass);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
