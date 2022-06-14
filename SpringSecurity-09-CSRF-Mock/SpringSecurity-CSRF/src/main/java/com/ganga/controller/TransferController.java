package com.ganga.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    @PostMapping("/zhuan")
    public String transfer(){
        System.out.println("模拟：没执行一次，完成一次转账");
        return "模拟：没执行一次，完成一次转账 <br> 转账成功";
    }

}
