package com.ganga.config;

import com.ganga.sercurity.filter.LoginVerifyCodeFilter;
import com.ganga.service.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginUserDetailsService userDetailsService;
    private final DataSource dataSource;
    @Autowired
    public SecurityConfig(LoginUserDetailsService userDetailsService,DataSource dataSource) {
        this.userDetailsService = userDetailsService;
        this.dataSource = dataSource;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**/*.html").permitAll()
                .antMatchers("/**/*.css").permitAll()
                .antMatchers("/**/*.js").permitAll()
                .antMatchers("/**/*.jpg").permitAll()
                .mvcMatchers("/ico.jpg").permitAll()
                .mvcMatchers("/login").permitAll()
                .mvcMatchers("/doLogin").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .passwordParameter("passwd")
                .usernameParameter("uname")
                .loginProcessingUrl("/doLogin")
                .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .tokenRepository(persistentTokenRepository())
                .and()
                .csrf().disable();

        //替换拦截
        //http.addFilterAt(loginVerifyCodeFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    /**
     * 自定义拦截器 formLogin拦截  交给工厂管理
     *
     * @return UsernamePasswordAuthenticationFilter 实例
     * @throws Exception
     */
    @Bean
    public LoginVerifyCodeFilter loginVerifyCodeFilter() throws Exception {
        LoginVerifyCodeFilter loginVerifyCodeFilter = new LoginVerifyCodeFilter();
        //自定义认证路径 认证参数
        loginVerifyCodeFilter.setFilterProcessesUrl("/doLogin");
        loginVerifyCodeFilter.setUsernameParameter("uname");
        loginVerifyCodeFilter.setPasswordParameter("passwd");
        loginVerifyCodeFilter.setVerifyCodeParameter("verifyCode");
        //指定 认证匹配器
        loginVerifyCodeFilter.setAuthenticationManager(authenticationManagerBean());
        //认证成功后的处理
        loginVerifyCodeFilter.setAuthenticationSuccessHandler((req,resp,authentication)->{
            resp.sendRedirect("/index");
        });
        //认证失败后的处理
        loginVerifyCodeFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.sendRedirect("/login");
        });

        //交给工厂
        return loginVerifyCodeFilter;
    }


    /**
     * RememberMe 持久化认证令牌
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource); //指定数据源
        jdbcTokenRepository.setCreateTableOnStartup(false); //第一次 创建表时 为true
        return jdbcTokenRepository;
    }


    /**
     * 自定义 认证管理器
     * auth.userDetailsService(userDetailsService); 指定自定义的数据源对象
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    /**
     * 将自定义的 认证管理器 暴露工厂外部
     *
     * @return 自定义的 AuthenticationManager
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
