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
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * Spring Security 相关配置
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

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
                .rememberMe()
                .rememberMeParameter(PersistentTokenBasedRememberMeServices.DEFAULT_PARAMETER)
                .rememberMeServices(rememberMeServices())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and()
                .csrf().disable()
                .sessionManagement() //开启会话管理
                .maximumSessions(1) //最大统一用户 最大登录会话数量
                //.expiredUrl("/login")
                .expiredSessionStrategy(event -> {
                    event.getResponse().sendRedirect("/login");
                });

        http.addFilterAt(verifyCodeFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public VerifyCodeFilter verifyCodeFilter() throws Exception {
        VerifyCodeFilter verifyCodeFilter = new VerifyCodeFilter();
        verifyCodeFilter.setFilterProcessesUrl("/doLogin");
        verifyCodeFilter.setUsernameParameter("uname");
        verifyCodeFilter.setPasswordParameter("passwd");
        //开启renameMe
        verifyCodeFilter.setRememberMeServices(rememberMeServices());
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


    /**
     * RememberMeServices 实现 注入bean
     * @return
     */
    @Bean
    public RememberMeServices rememberMeServices(){
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
                UUID.randomUUID().toString(),userDetailService,persistentTokenRepository()
        );
        return rememberMeServices;
    }

    /**
     * 数据库持久化令牌
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
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
