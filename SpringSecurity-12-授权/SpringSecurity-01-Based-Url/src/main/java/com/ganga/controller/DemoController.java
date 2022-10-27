package com.ganga.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/admin")
    public String admin() {
        return "admin ok";
    }

    @GetMapping("/user")
    public String user() {
        return "user ok";
    }

    @GetMapping("/getInfo")
    public String getInfo() {
        return "getInfo ok";
    }

    @GetMapping("/aut01")
    public String aut01() {
        return "aut01 ok";
    }

    @GetMapping("/aut02")
    public String aut02() {
        return "aut02 ok";
    }
}