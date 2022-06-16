package com.ganga.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//方式一: 使用 @CrossOrigin 注解解决跨域问题

@RestController
//@CrossOrigin //加载类上 表示该类中的所有 资源 都允许跨域
public class TestController {

    /**
     * // @CrossOrigin 中的属性有：                                             <br><br>
     * alowCredentials：浏览器是否应当发送凭证信息，如 Cookie。                   <br>
     * allowedHeaders： 请求被允许的请求头字段， * 表示所有字段。                  <br>
     * exposedHeaders：哪些响应头可以作为响应的⼀部分暴露出来。                    <br>
     *                                                                        <br>
     * maxAge：预检请求的有效期，有效期内不必再次发送预检请求，默认是 1800 秒。      <br>
     * methods：允许的请求⽅法， * 表示允许所有⽅法。                             <br>
     * origins：允许的域， * 表示允许所有域。                                    <br><br>
     *
     */
    @GetMapping("/test")
    //@CrossOrigin //加在方法上 表示该方法允许跨域
    //@CrossOrigin(origins = {"*"}) //还可以设置相应的属性
    public String test(){
        System.out.println("test ok...");
        return "test ok...";
    }



}
