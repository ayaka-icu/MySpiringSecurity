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

        //查询用户认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        System.out.println("用户名：" + user.getUsername());
        System.out.println("权限信息：" + authentication.getAuthorities());

        return "Hello SpringSecurity...";
    }
}
