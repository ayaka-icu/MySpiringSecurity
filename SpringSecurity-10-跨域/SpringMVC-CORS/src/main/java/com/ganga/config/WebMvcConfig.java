package com.ganga.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 方式二: 在 SpringMvc 配置文件中 重写addCorsMappings(CorsRegistry registry)方法
 *          添加跨域资源 addMapping()
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //允许跨域的资源  /** : 所有资源均可
                .allowedMethods("*") //允许那些提交方式可以跨域   * : 所有方法
                .allowedOrigins("*")  //允许的域， * 表示允许所有域。
                .allowedHeaders("*") //请求被允许的请求头字段， * 表示所有字段。
                .exposedHeaders("*") //哪些响应头可以作为响应的⼀部分暴露出来。
                .allowCredentials(false) //是否需要凭证 false不需要
                .maxAge(3600); //预检请求的有效期，有效期内不必再次发送预检请求，默认是 1800 秒。
    }
}
