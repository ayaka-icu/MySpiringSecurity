package com.ganga.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class VerifyCodeController {

    @Autowired
    private Producer producer;

    @RequestMapping("/ico.jpg")
    public void verify(HttpServletResponse response, HttpSession session) throws IOException {

        //生成验证码
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);
        //验证码放入session中 或 redis 中
        session.setAttribute("verifyCode",text);
        //相应给用户
        response.setContentType("image/png");
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image,"jpg",outputStream);
        System.out.println(text);
    }

}
