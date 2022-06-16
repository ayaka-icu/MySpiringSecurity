package com.ganga.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/*.css").permitAll()
                .antMatchers("/js/*.js").permitAll()
                .antMatchers("/imgs/*.jpg").permitAll()
                .mvcMatchers("/login").permitAll()
                .mvcMatchers("/login.html").permitAll()
                //给 hello 授权 需要 admin权限才能访问   TODO: 下节在学！
                .mvcMatchers("/hello").hasRole("admin")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/doLogin")
                .defaultSuccessUrl("/hello")
                .and()
                //必须先加上这个方法 这个方法下面的表示添加异常
                .exceptionHandling()
                //未认证时的异常
                .authenticationEntryPoint((request, response, exception) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("尚未认证，请进行认证操作！");
                })
                //无权限的异常
                .accessDeniedHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("无权访问!");
                })
                .and()
                .csrf().disable();

    }

    @Bean
    public UserDetailsManager userDetailsManager(){

        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(User.withUsername("ganga").password("{noop}123").roles("admin").build());
        userDetailsManager.createUser(User.withUsername("user").password("{noop}123").roles("user").build());
        return userDetailsManager;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsManager());
    }


}
