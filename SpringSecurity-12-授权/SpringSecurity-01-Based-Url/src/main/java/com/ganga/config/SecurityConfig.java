package com.ganga.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * 基于 URL 权限管理
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        userDetailsService.createUser(User.withUsername("ganga").password("{noop}123").roles("ADMIN").build());
        userDetailsService.createUser(User.withUsername("gangale").password("{noop}123").roles("USER").build());
        userDetailsService.createUser(User.withUsername("gangadi").password("{noop}123").roles("USER","ADMIN").build());
        userDetailsService.createUser(User.withUsername("gangajiang").password("{noop}123").authorities("READ_BOOK").build());
        userDetailsService.createUser(User.withUsername("ganga...").password("{noop}123").authorities("READ_LOVE").build());

        return userDetailsService;
    }

    /**
     * hasAuthority(String authority)：          当前用户是否具备指定权限                                       <br>
     * hasAnyAuthority(String... authorities)：  当前用户是否具备指定权限中任意一个                              <br>
     * hasRole(String role)：                    当前用户是否具备指定角色                                      <br>
     * hasAnyRole(String... roles)：             当前用户是否具备指定角色中任意一个                              <br>
     * permitAll()：                             放行所有请求/调用                                             <br>
     * denyAll()：                               拒绝所有请求/调用                                             <br>
     * isAnonymous()：                           当前用户是否是一个匿名用户                                     <br>
     * isAuthenticated()：                       当前用户是否已经认证成功                                       <br>
     * isRememberMe()：                          当前用户是否通过 Remember-Me 自动登录                          <br>
     * isFullyAuthenticated()：                  当前用户是否既不是匿名用户又不是通过 Remember-Me 自动登录的       <br><br>
     *
     * hasPermission(Object targetId, Object permission); 当前用户是否具备指定目标的指定权限信息                 <br>
     * hasPermission(Object targetId, String targetType, Object permission); 当前用户是否具备指定目标的指定     <br>
     *
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .mvcMatchers("/user").hasRole("USER")
                .mvcMatchers("/getInfo").hasAnyRole("ADMIN","USER")
                .mvcMatchers("/aut01").hasAuthority("READ_BOOK")
                .mvcMatchers("/aut02").hasAnyAuthority("READ_BOOK","READ_LOVE")
                .anyRequest().authenticated()
                .and().formLogin()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("该账户未被授权！");
                })
                .and()
                .csrf().disable();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }
}
