package com.ganga.config;

import com.ganga.security.UserDetailsServiceImpl;
import com.ganga.security.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.UUID;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //注入 自定义的UserDetailsService
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    //注入 数据源
    @Autowired
    private DataSource dataSource;


    /**
     * 权限相关  <br>
     * <hr>
     * <br>
     * 基于url的授权方式：                                                                                    <br><br>
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
     * <p>
     * hasPermission(Object targetId, Object permission); 当前用户是否具备指定目标的指定权限信息                 <br>
     * hasPermission(Object targetId, String targetType, Object permission); 当前用户是否具备指定目标的指定     <br>
     *
     * <hr>
     *
     * <br>  基于 方法 权限管理
     * <br>
     * <br>  使用注解：@EnableGlobalMethodSecurity  在SecurityConfig
     * <br>
     * <br>  perPostEnabled:  开启 Spring Security 提供的四个权限注解，@PostAuthorize、@PostFilter、@PreAuthorize 以及@PreFilter。
     * <br>  securedEnabled:  开启 Spring Security 提供的 @Secured 注解支持，该注解不支持权限表达式
     * <br>  jsr250Enabled:   开启 JSR-250 提供的注解，主要是@DenyAll、@PermitAll、@RolesAll 同样这些注解也不不持权限表达式
     * <br>
     * <br>  以上注解含义如下:
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
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**/*.css").permitAll()
                .antMatchers("/**/*.jpg").permitAll()
                .antMatchers("/**/*.js").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/login.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                //.loginProcessingUrl("/doLogin")
                //.usernameParameter("username")
                //.passwordParameter("password")
                //.successForwardUrl("hello.html")
                //.defaultSuccessUrl("/hello.html")
                //.failureUrl("/login.html")
                //.failureForwardUrl("/login.html")
                .and()
                .rememberMe()
                //.rememberMeParameter("remember-me")
                //.rememberMeServices(rememberMeServices())
                .tokenRepository(persistentTokenRepository()) //自定义 login 也要 写 .tokenRepository()
                .and()
                .logout()
                //.logoutUrl("/doLogout")
                .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/aaa", HttpMethod.GET.toString()),
                        new AntPathRequestMatcher("/logout", HttpMethod.POST.toString())
                ))
                //.logoutSuccessUrl("/login.html")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.sendRedirect("/login.html");
                })
                .and()
                .exceptionHandling() //这里并不是 标准的 传统web解决方案  传统解决使用页面跳转即可 更简单
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
                .and().csrf()
                .and()
                .cors()
                .configurationSource(configurationSource());

        //替换成 自定义实现的 UsernamePasswordAuthenticationFilter
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    /**
     * 创建封装好的 自定义的 UsernamePasswordAuthenticationFilter对象 <br>
     * 设置 formLogin 属性
     *
     * @return
     */
    public LoginFilter loginFilter() throws Exception {
        LoginFilter login = new LoginFilter();
        //设置认证地址 及 参数：用户名 密码 验证码
        login.setFilterProcessesUrl("/doLogin");
        login.setUsernameParameter("username");
        login.setPasswordParameter("password");
        login.setVerifyCode("verifyCode");
        //设置 认证管理器
        login.setAuthenticationManager(authenticationManagerBean());
        //设置 认证成功后的操作 使用 λ 表达式
        login.setAuthenticationSuccessHandler((req, resp, auth) -> {
            resp.sendRedirect("/hello.html");
        });
        //设置 认证失败后的操作 使用 λ 表达式
        login.setAuthenticationFailureHandler(((req, resp, exception) -> {
            resp.sendRedirect("/login.html");
        }));
        //设置 RememberMe
        login.setRememberMeServices(rememberMeServices());
        //返回 自定义过滤器
        return login;
    }

    CorsConfigurationSource configurationSource() {

        // 跨域设置
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*")); //允许的域， * 表示允许所有域。
        corsConfiguration.setAllowedHeaders(Arrays.asList("*")); //请求被允许的请求头字段， * 表示所有字段。
        corsConfiguration.setAllowedMethods(Arrays.asList("*")); //允许那些提交方式可以跨域   * : 所有方法
        corsConfiguration.setMaxAge(3600L);

        // 那些地址允许跨域
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    /**
     * 自定义 RememberMeServices   <br>
     * class: PersistentTokenBasedRememberMeServices
     *
     * @return
     */
    @Bean
    RememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices ptb = new PersistentTokenBasedRememberMeServices(
                UUID.randomUUID().toString(), //生成一个uuid
                userDetailsService, //设置 自定义 userDetailsService
                persistentTokenRepository() //持久化令牌 技术 使用的是自定义
        );

        return ptb;
    }

    /**
     * 自定义持久化 令牌  使用数据库实现 <br>
     * Class: JdbcTokenRepositoryImpl
     *
     * @return
     */
    @Bean
    PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource); //配置数据源
        jdbcTokenRepository.setCreateTableOnStartup(false);//第一次使用 true 创建数据库 创建之后就可以 设置为 false 了
        return jdbcTokenRepository;
    }


    //创建自定义 认证管理器
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    //将自定义的认证管理器 暴露出来
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
