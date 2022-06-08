package com.ganga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserAutController {

    @RequestMapping("user")
    public String user(){
        return "user";
    }

}
