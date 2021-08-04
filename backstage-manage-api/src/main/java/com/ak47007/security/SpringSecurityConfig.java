package com.ak47007.security;

import com.ak47007.constant.AuthApiConstant;
import com.ak47007.constant.OpenApiConstant;
import com.ak47007.mapper.SysAuthorityApiMapper;
import com.ak47007.model.vo.AuthorityApiVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author AK47007
 * @date 2019/12/7
 * Describe:
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 自定义登录认证
     */
    @Resource
    private CustomerAuthenticationProvider customerAuthenticationProvider;

    /**
     * 自定义登录成功处理器
     */
    @Resource
    private CustomerAuthenticationSuccessHandler customerAuthenticationSuccessHandler;

    /**
     * 自定义登录失败处理器
     */
    @Resource
    private CustomerAuthenticationFailureHandler customerAuthenticationFailureHandler;

    /**
     * 自定义注销处理器
     */
    @Resource
    private CustomerLogoutSuccessHandler customerLogoutSuccessHandler;


    /**
     * token处理
     */
    @Resource
    private CustomerOncePerRequestFilter customerOncePerRequestFilter;

    /**
     * 未登录处理
     */
    @Resource
    private CustomerAuthenticationEntryPoint customerAuthenticationEntryPoint;

    /**
     * 没有权限
     */
    @Resource
    private CustomerAccessDeniedHandler customerAccessDeniedHandler;

    @Resource
    private SysAuthorityApiMapper sysAuthorityApiMapper;


    /**
     * 配置用户与角色
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 添加自定义登录认证
        auth.authenticationProvider(customerAuthenticationProvider);
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 去除 ROLE_ 前缀
        return new GrantedAuthorityDefaults("");
    }


    /**
     * 配置安全请求
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<AuthorityApiVO> authorityApis = sysAuthorityApiMapper.findAll();

        // 开启跨域(前后端分离需要),开启后配置CorsConfig类
        http.cors();

        //关闭csrf
        http.csrf().disable()
                //未登录结果处理
                .httpBasic().authenticationEntryPoint(customerAuthenticationEntryPoint).and()
                //权限不足结果处理
                .exceptionHandling().accessDeniedHandler(customerAccessDeniedHandler).and()
                // 权限管理设置
                .authorizeRequests()
                // 处理跨域请求中的Preflight请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
        // 动态配置权限
        // TODO 但页面上修改后需要重启刷新配置,还在找解决方案
        Map<String,List<AuthorityApiVO>> group = authorityApis.stream().collect(Collectors.groupingBy(AuthorityApiVO::getApiUrl));
        for (String apiUrl : group.keySet()) {
            List<AuthorityApiVO> authorityApiVOS = group.get(apiUrl);
            List<String> authorityNames = authorityApiVOS.stream().map(AuthorityApiVO::getAuthorityNameEn).collect(Collectors.toList());
            http.authorizeRequests().antMatchers(apiUrl).hasAnyAuthority(authorityNames.stream().toArray(String[]::new));
        }


        http.authorizeRequests().anyRequest().authenticated()
                //自定义登录请求路径(post请求)
                .and().formLogin()
                // 两个参数分别对应实现了UserDetails类的属性
                .usernameParameter(AuthApiConstant.USER_NAME).passwordParameter(AuthApiConstant.USER_PASS)
                // 登录接口
                .loginProcessingUrl(AuthApiConstant.API_LOGIN)
                //验证成功处理器
                .successHandler(customerAuthenticationSuccessHandler)
                //验证失败处理器
                .failureHandler(customerAuthenticationFailureHandler).permitAll()
                //关闭拦截未登录自动跳转,改为返回json信息
                .and().exceptionHandling()
                //开启自动配置的注销功能
                .and().logout().logoutUrl(AuthApiConstant.API_LOGOUT)
                //注销处理器
                .logoutSuccessHandler(customerLogoutSuccessHandler).permitAll();
        //在UsernamePasswordAuthenticationFilter之前增加鉴权过滤器（需要修改，下面注销时解释）
        http.addFilterBefore(customerOncePerRequestFilter,UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        // 开放的接口(忽略权限的接口)
        web.ignoring().antMatchers(OpenApiConstant.API_CAPTCHA,OpenApiConstant.API_GET_MENU,"/doc.html","/webjars/**","/swagger-resources","/v2/**");
    }

}
