package com.ganga.security.filter;

import com.ganga.security.exception.VerifyCodeException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VerifyCodeFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "kaptcha";

    private String verifyCodeParameter = SPRING_SECURITY_FORM_USERNAME_KEY;

    public String getVerifyCodeParameter() {
        return verifyCodeParameter;
    }

    public void setVerifyCodeParameter(String verifyCodeParameter) {
        verifyCodeParameter = verifyCodeParameter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //1.判断是否为 post 请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //2.判断验证码相关 是否为空 是否正确...
        //从请求中获取验证码
        String verifyCodeUser = request.getParameter(verifyCodeParameter);
        //从session中获取验证码
        String verifyCodeSession = (String) request.getSession().getAttribute(verifyCodeParameter);
        //判断
        if (!ObjectUtils.isEmpty(verifyCodeUser)
                && !ObjectUtils.isEmpty(verifyCodeSession)
                && verifyCodeUser.equalsIgnoreCase(verifyCodeSession)){
            //回调父类
            return super.attemptAuthentication(request, response);
        }

        //抛出异常
        throw new VerifyCodeException("码验证不匹配！");

    }
}
