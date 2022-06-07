package com.ganga.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    //认证request
                .authorizeRequests()
                //开放这些资源 不用进行认证
                .antMatchers("/*.html").permitAll()
                .antMatchers("/**/*.js").permitAll()
                .antMatchers("/**/*.css").permitAll()
                .antMatchers("/**/*.jpg").permitAll()
                .antMatchers("/login").permitAll()
                //除上面以外 都需要认证
                .anyRequest().authenticated()
                .and()
                .formLogin() //认证方式为 form 表单方式
                .loginPage("/login.html") //指定认证页面 这个页面要开放不被认证！
                .loginProcessingUrl("/doLogin") //要指定认证页面post 提交到哪接收认证 相当于doLogin
                .usernameParameter("uname") //设置表单用户名属性 username -> uname
                .passwordParameter("passwd") //设置表单密码属性 password -> passwd
                //.successForwardUrl("/index") // 当表单认证成功后 forward转发 的路径
                //.defaultSuccessUrl("/index",false)// 当表单认证成功后 redirect重定向 的位置, 同时 第二参数默认为 false 时 验证成功后返回拦截前的地址
                .successHandler(new MyAuthenticationSuccessHandler()) //前后端分离解决方案
                .failureForwardUrl("/login.html") //配置错误 forward转发
                .failureUrl("/login.html") //配置错误解析 redirect重定向
                //.failureHandler()  // TODO:前后端分离解决方案 明天学
                .and()
                .csrf().disable() //TODO: 这里关闭了跨域问题 以后学
                ;

    }
}
