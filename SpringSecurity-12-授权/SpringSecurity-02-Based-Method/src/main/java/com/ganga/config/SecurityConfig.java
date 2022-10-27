package com.ganga.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 *
 * <br>  基于 方法 权限管理
 * <br>
 * <br>  使用注解：@EnableGlobalMethodSecurity  在SecurityConfig
 * <br>
 * <br>  perPostEnabled:  开启 Spring Security 提供的四个权限注解，@PostAuthorize、@PostFilter、@PreAuthorize 以及@PreFilter。
 * <br>  securedEnabled:  开启 Spring Security 提供的 @Secured 注解支持，该注解不支持权限表达式
 * <br>  jsr250Enabled:   开启 JSR-250 提供的注解，主要是@DenyAll、@PermitAll、@RolesAll 同样这些注解也不不持权限表达式
 * <br>
 * <br>  以上注解含 义如下:
 * <br>  - @PostAuthorize：  在目标方法执行之后进行权限校验。
 * <br>  - @PostFilter：     在目标方法执行之后对方法的返回结果进行过滤。
 * <br>  - @PreAuthorize：   在目标方法执行之前进行权限校验。
 * <br>  - @PreFilter：      在目标方法执行之前对放法参数进行过滤。
 * <br>  - @secured：        访问目标方法必须具各相应的角色。
 * <br>  - @DenyAll：        拒绝所有访问。
 * <br>  - @PermitAll：      允许所有访问。
 * <br>  - @RolesAllowed：   访问目标方法必须具备相应的角色。
 * <br>
 * <br> 这些基于方法的权限管理相关的注解，一般来说只要设置prePostEnabled=true prePostEnabled=true 就够用了。
 *
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
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


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
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
