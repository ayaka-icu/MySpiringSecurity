package com.ganga.config;

import com.ganga.security.filter.VerifyCodeFilter;
import com.ganga.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                .mvcMatchers("/login").permitAll()
                .antMatchers("/**/*.html").permitAll()
                .antMatchers("/**/*.js").permitAll()
                .antMatchers("/**/*.css").permitAll()
                .antMatchers("/**/*.jpg").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
            /*  .loginProcessingUrl("/doLogin")
                .usernameParameter("uname")
                .passwordParameter("passwd")
                .defaultSuccessUrl("/index",true) //添加验证码时 自定义了这里就不用了 */
                .failureUrl("/login")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/imgs/cy.jpg")
                .and()
                .csrf().disable();

        http.addFilterAt(verifyCodeFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public VerifyCodeFilter verifyCodeFilter() throws Exception {
        VerifyCodeFilter verifyCodeFilter = new VerifyCodeFilter();
        verifyCodeFilter.setFilterProcessesUrl("/doLogin");
        verifyCodeFilter.setUsernameParameter("uname");
        verifyCodeFilter.setPasswordParameter("passwd");
        //指定认证管理器
        verifyCodeFilter.setAuthenticationManager(authenticationManagerBean());
        //成功后的操作
        verifyCodeFilter.setAuthenticationSuccessHandler((req,resp,authentication)->{
            resp.sendRedirect("/index");
        });
        //失败后的操作
        verifyCodeFilter.setAuthenticationFailureHandler((req,resp,exception)->{
            resp.sendRedirect("/login");
        });

        //载入Bean
        return verifyCodeFilter;
    }


    private final MyUserDetailService userDetailService;
    @Autowired
    public SecurityConfig(MyUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }
}
