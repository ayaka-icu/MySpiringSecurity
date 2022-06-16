package com.ganga.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ganga.domin.User;
import com.ganga.security.filter.LoginVerifyCodeFilter;
import com.ganga.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security 相关配置
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 配置 认证规则 和 自定义登录界面
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/doLogin").permitAll()
                .mvcMatchers("/ico.jpg").permitAll()
                .anyRequest().authenticated() //前后端分离 请求都需要认证
                .and()
                .formLogin()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((req,resp,exception)->{
                    resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    resp.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401
                    resp.getWriter().write("请认证之后再访问！");
                    // TODO: 关于异常以后学习~
                })
                .and()
                .logout()
                .logoutSuccessHandler((req,resp,authentication)->{
                    Map<String,Object> map = new HashMap<>();
                    map.put("msg","注销登录成功");
                    map.put("用户信息",authentication.getPrincipal());
                    resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    resp.setStatus(HttpStatus.OK.value());
                    String s = new ObjectMapper().writeValueAsString(map);
                    resp.getWriter().write(s);
                })
                .and()
                .csrf().disable();

        http.addFilterAt(loginVerifyCodeFilter(), UsernamePasswordAuthenticationFilter.class);

    }


    /**
     * 在这里注入 自定义的LoginFilter 交给工厂管理
     * @return
     */
    @Bean
    public LoginVerifyCodeFilter loginVerifyCodeFilter() throws Exception {
        LoginVerifyCodeFilter loginFilter = new LoginVerifyCodeFilter();
        //1.修改认证地址信息 或 参数信息
        loginFilter.setFilterProcessesUrl("/doLogin");
        loginFilter.setUsernameParameter("username");
        loginFilter.setPasswordParameter("password");
        loginFilter.setVerifyCodeKey("verifyCode");
        //2.设置认证管理器
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        //3.认证成功后的设置
        loginFilter.setAuthenticationSuccessHandler((req,resp,authentication)->{
            Map<String,Object> map = new HashMap<>();
            map.put("msg","登陆成功");
            User user = (User) authentication.getPrincipal();
            map.put("用户信息", user.getUsername());
            resp.setStatus(HttpStatus.OK.value());
            resp.setContentType("application/json;charset=UTF-8");
            String s = new ObjectMapper().writeValueAsString(map);
            resp.getWriter().write(s);
        });
        //4.认证失败后的设置
        loginFilter.setAuthenticationFailureHandler((req,resp,exception)->{
            Map<String,Object> map = new HashMap<>();
            map.put("msg","登陆失败");
            map.put("失败信息", exception.getMessage());
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            resp.setContentType("application/json;charset=UTF-8");
            String s = new ObjectMapper().writeValueAsString(map);
            resp.getWriter().write(s);
        });
        //注入Bean
        return loginFilter;
    }


    /**
     * 注入之定义的 UserDetailService对象
     */
    private final MyUserDetailService userDetailService;
    @Autowired
    public SecurityConfig(MyUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    /**
     * 自定义 AuthenticationManager
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }

    /**
     * 暴露自定义 AuthenticationManager
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
