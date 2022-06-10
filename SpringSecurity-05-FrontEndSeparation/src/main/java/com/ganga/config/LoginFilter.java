package com.ganga.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 自定义配置 UsernamePasswordAuthenticationFilter
 */
//@Configuration
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("=========================");
        //1.判断请求方式是否为 post 方式
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //2.判断请求体参数是否为 json 格式, Application/json
        if (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)){
            System.out.println("进入");
            //3.获取json数据 并取出账户名和密码 进行认证
            try {
                Map<String,String> userInfo = new ObjectMapper().readValue(request.getInputStream(),Map.class);
                String username = userInfo.get(getUsernameParameter());
                String password = userInfo.get(getPasswordParameter());
                UsernamePasswordAuthenticationToken authRequest
                        = UsernamePasswordAuthenticationToken.unauthenticated(username,password);
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return super.attemptAuthentication(request,response);
    }
}
