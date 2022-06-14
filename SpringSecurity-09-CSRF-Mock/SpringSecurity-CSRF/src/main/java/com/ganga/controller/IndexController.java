package com.ganga.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController{

    @GetMapping("/index")
    public String index(){
        System.out.println("index ok ...");
        return "<h2>index ok ...</h2>";
    }

}
