package com.ganga.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityController extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .rememberMe()
                .and()
                .csrf(); //默认是开启跨站请求攻击的
                //.disable(); //先关闭 csrf 跨站请求伪装 攻击

        // 令牌同步模式  <input name="_csrf" type="hidden" value="9d7db596-4b72-4d76-8ace-d86d5bf2b74a">
        // 跨请求失败   There was an unexpected error (type=Forbidden, status=403).
    }
}
