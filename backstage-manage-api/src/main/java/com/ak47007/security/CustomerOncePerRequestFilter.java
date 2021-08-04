package com.ak47007.security;

import com.ak47007.constant.AuthApiConstant;
import com.ak47007.constant.OpenApiConstant;
import com.ak47007.enums.ResultEnum;
import com.ak47007.model.SysAuthority;
import com.ak47007.model.SysRole;
import com.ak47007.model.SysUser;
import com.ak47007.model.base.Result;
import com.ak47007.service.ValidateCodeService;
import com.ak47007.utils.ResponseUtil;
import com.ak47007.utils.UserUtil;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author AK47007
 * @date 2019/12/9
 * Describe: 请求过滤,每个请求都会经过这里
 */
@Component
@AllArgsConstructor
public class CustomerOncePerRequestFilter extends OncePerRequestFilter {


    private final UserUtil userUtil;

    private final ValidateCodeService validateCodeService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String currUri = request.getRequestURI();

        // 登录接口需要先验证验证码是否正确
        if (Strings.isNotBlank(currUri) && currUri.equals(AuthApiConstant.API_LOGIN)) {
            String codeKey = request.getParameter("codeKey");
            String codeValue = request.getParameter("code");
            Result<?> result = validateCodeService.check(codeKey, codeValue);
            if (result.getResultCode().equals(ResultEnum.ERROR.getResultCode())) {
                ResponseUtil.response(result, response);
                return;
            }

        }
        // 如果保存的用户信息为空的话再去读取Redis
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SysUser user = userUtil.getUser(request);
            // 从redis中读取用户信息,如果为空的话跳转到用户未登录处理器
            if (user != null) {
                SysRole role = user.getRole();
                UserEntity userEntity = new UserEntity(user);
                if (role != null) {
                    user.setRole(role);
                    // 权限集合 如果他们提供了正确的用户名和密码并且启用了用户，则应授予调用者权限。不是空的。
                    ArrayList<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
                    // 角色需要ROLE_前缀
                    grantedAuthorityList.add(new SimpleGrantedAuthority(role.getRoleNameEn()));
                    if (role.getAuthorityList() != null && role.getAuthorityList().size() > 0) {
                        // 找出该角色所拥有的权限
                        List<SysAuthority> authorityList = role.getAuthorityList();
                        for (SysAuthority authority : authorityList) {
                            grantedAuthorityList.add(new SimpleGrantedAuthority(authority.getAuthorityNameEn()));
                        }
                        userEntity.setAuthorities(grantedAuthorityList);
                    }
                }
                // // 保存用户信息
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEntity.getUsername(), userEntity.getPassword(), userEntity.getAuthorities());
                // // 必须得保存进去,不然Spring Security 找不到你的权限.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
