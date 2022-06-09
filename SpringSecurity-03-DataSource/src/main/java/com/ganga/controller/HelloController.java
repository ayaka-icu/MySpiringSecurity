package com.ganga.controller;

import com.ganga.domin.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        System.out.println("Hello SpringSecurity...");

        //获取登录的用户数据
        //从SecurityContextHolder 中获取

        /*
        MODE_THREADLOCAL 此线程 单线程
        MODE_INHERITABLETHREADLOCAL 子线程
        MODE_GLOBAL 全局
        */
        //默认情况下时： MODE_THREADLOCAL 此线程 单线程
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        System.out.println("用户身份 :" + user);
        System.out.println("用户名 :" + user.getUsername());
        System.out.println("用户权限 :" + authentication.getAuthorities());

        //MODE_INHERITABLETHREADLOCAL 子线程
        new Thread(()->{
            Authentication aut = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("子线程：认证成功对象:" + aut);
        }).start();//别忘了启动多线程
        //开启方式：虚拟机选项中设置： -Dspring.security.strategy=MODE_INHERITABLETHREADLOCAL

        return "Hello SpringSecurity...";
    }
}
