package com.ganga.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests() //认证request
                //开放这些资源 不用进行认证
                .mvcMatchers("/login.html").permitAll()
                .antMatchers("/*.html").permitAll()
                .antMatchers("/**/*.js").permitAll()
                .antMatchers("/**/*.css").permitAll()
                .antMatchers("/**/*.jpg").permitAll()
                .antMatchers("/login").permitAll()
                //除上面以外 都需要认证
                .anyRequest().authenticated()
                .and()
                .formLogin() //认证方式为 form 表单方式
                .loginPage("/login") //指定认证页面 这个页面要开放不被认证！
                .loginProcessingUrl("/doLogin") //要指定认证页面post 提交到哪接收认证 相当于doLogin
                .usernameParameter("uname") //设置表单用户名属性 username -> uname
                .passwordParameter("passwd") //设置表单密码属性 password -> passwd
                //.successForwardUrl("/index") // 当表单认证成功后 forward转发 的路径
                //.defaultSuccessUrl("/index",false)// 当表单认证成功后 redirect重定向 的位置, 同时 第二参数默认为 false 时 验证成功后返回拦截前的地址
                .successHandler(new MyAuthenticationSuccessHandler()) //前后端分离解决方案
                .failureForwardUrl("/login") //配置错误 forward转发
                //.failureUrl("/login") //配置错误解析 redirect重定向
                .failureHandler(new MyAuthenticationFailureHandler()) //前后端分离解决方案
                .and()
                .logout() // 自定义注销认证
                //.logoutUrl("/logout") // 指定注销当前认证的接口地址  默认必须时GET方式
                .logoutRequestMatcher(new OrRequestMatcher( //Or/And... 多个验证 or任意一个即可 and都满足  此外可以解决请求方式
                        new AntPathRequestMatcher("/aa","GET"), //get方式
                        new AntPathRequestMatcher("/logout","POST") //post
                ))
                .invalidateHttpSession(true) // 是否清除当前的session 默认时true
                .clearAuthentication(true)   // 是否清除当前的认证信息 默认时true
                .logoutSuccessUrl("/login")  // 注销认证后 要跳转的页面
                .logoutSuccessHandler(new MyLogoutSuccessHandler()) //解决前后端分离方案
                .and()
                .csrf().disable() //TODO: 这里关闭了跨域问题 以后学
                ;

    }
}
