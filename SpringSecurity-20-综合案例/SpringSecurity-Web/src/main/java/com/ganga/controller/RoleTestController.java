package com.ganga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class RoleTestController {

    @RequestMapping("/admin")
    public String admin() {
        return "admin ok";
    }

    @RequestMapping("/user")
    public String user() {
        return "user ok";
    }

    @RequestMapping("/getInfo")
    public String getInfo() {
        return "getInfo ok";
    }

    @RequestMapping("/aut01")
    public String aut01() {
        return "aut01 ok";
    }

    @RequestMapping("/aut02")
    public String aut02() {
        return "aut02 ok";
    }

}
