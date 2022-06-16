package com.ganga.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

/**
 * SpringSecurity 核心管理配置类
 *
 *  重写
 *  @Override
 *  protected void configure(HttpSecurity http) throws Exception
 *  自定义认证规则
 *
 *  重写
 *  @Override
 *  public void configure(AuthenticationManagerBuilder builder) throws Exception
 *  自定义AuthenticationManager
 *  builder.userDetailsService(userDetailsService) 设置自定义数据源
 *
 *
 */
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {


    /**
     * 自定义认证规则
     * @param http
     * @throws Exception
     */
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
                .csrf().disable() //TODO: 这里关闭了csrf问题 以后学
                ;
    }


    /*
    自定义数据源
     */

    //注入自定义的 UserDetailsService 自定义数据源信息
    private final MyUserDetailsService myUserDetailsService;
    @Autowired
    public WebSecurityConfigurer(MyUserDetailsService myUserDetailsService){
        this.myUserDetailsService = myUserDetailsService;
    }


    //自定义AuthenticationManager 推荐
    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        //自定义一个 UserDetailsService 放入AuthenticationManagerBuilder中
        builder.userDetailsService(myUserDetailsService); //传入自定义的UserDetailsService对象
    }


    //覆写authenticationManagerBean() 方法  @Bean注入到容器中即可
    //直接调用覆盖 不需要调父类里的此方法即可   但是要加个@Bean注解
    @Override
    @Bean //此外, 还要@Bean注入到 Spring容器当中
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



//==========================下面是演示 所有情况 ==========================


/*    *//**
     * 方式一： 默认全局 AuthenticationManager
     *      springboot 对 security 进行自动配置时自动在工厂中创建一个全局
     * @param builder
     *//*
    @Autowired
    public void initialize(AuthenticationManagerBuilder builder) throws Exception {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        userDetailsService.createUser(User.withUsername("aaa").password("{noop}123").roles("admin").build());
        builder.userDetailsService(userDetailsService);
        builder.userDetailsService(userDetailsService());
        //循环依赖： Requested bean is currently in creation: Is there an unresolvable circular reference?
        //此时，这个工厂中的配置 就显得多余了 直接@Bean 上一个 UserDetailsService
    }

    *//**
     *
     * @return
     *//*
    @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        userDetailsService.createUser(User.withUsername("bbb").password("{noop}123").roles("admin").build());
        return userDetailsService;
    }


    *//**
     * 注入自定义的 UserDetailsService 自定义数据源信息
     *//*
    private final MyUserDetailsService myUserDetailsService;
    @Autowired
    public WebSecurityConfigurer(MyUserDetailsService myUserDetailsService){
        this.myUserDetailsService = myUserDetailsService;
    }

    *//**
     * 方式二： 自定义AuthenticationManager 推荐
     *      但是！并没有在工厂中暴露出来！只在工厂内部使用
     * @param builder
     *//*
    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        //自定义一个 UserDetailsService 放入AuthenticationManagerBuilder中
        builder.userDetailsService(myUserDetailsService); //传入自定义的UserDetailsService对象
    }

    *//**
     * 覆写authenticationManagerBean() 方法  @Bean注入到容器中即可
     * 作用：3用来将自定义的AuthenticationManager在工厂中进行暴露 可以在任何位置注入
     * @return
     * @throws Exception
     *//*
    @Override
    @Bean //此外, 还要@Bean注入到 Spring容器当中
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }*/


}
