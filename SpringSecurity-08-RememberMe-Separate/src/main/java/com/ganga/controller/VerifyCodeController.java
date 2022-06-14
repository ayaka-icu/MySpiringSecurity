package com.ganga.controller;

import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class VerifyCodeController {

    private final Producer producer;
    @Autowired
    public VerifyCodeController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/ico.jpg")
    public String verifyCode(HttpServletResponse response, HttpSession session) throws IOException {

        //1.生成验证码
        String code = producer.createText();
        session.setAttribute("verifyCode", code);//可以更换成 redis 实现
        System.out.println(session.getAttribute("verifyCode"));
        BufferedImage bi = producer.createImage(code);
        //2.写⼊内存
        FastByteArrayOutputStream fos = new  FastByteArrayOutputStream();
        ImageIO.write(bi, "png", fos);
        //3.⽣成 base64
        return Base64.encodeBase64String(fos.toByteArray());
    }

}
