package com.ganga.sercurity.filter;


import com.ganga.sercurity.exception.LonginVerifyCodeException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginVerifyCodeFilter extends UsernamePasswordAuthenticationFilter {

    public static final String FORM_VERIFY_CODE_KEY = "verifyCode";

    private String verifyCodeParameter = FORM_VERIFY_CODE_KEY;

    public String getVerifyCodeParameter() {
        return verifyCodeParameter;
    }

    public void setVerifyCodeParameter(String verifyCodeParameter) {
        this.verifyCodeParameter = verifyCodeParameter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //1.获取用户输入的用户名 密码 验证码 等信息
        String username = request.getParameter(getUsernameParameter());
        String password = request.getParameter(getPasswordParameter());
        String verifyCodeUser = request.getParameter(getVerifyCodeParameter());
        //2.从Session中 或 Redis等 中获取验证码
        String verifyCodeSession = (String) request.getSession().getAttribute(getVerifyCodeParameter());
        //3.判断验证码是否正确
        if (!ObjectUtils.isEmpty(verifyCodeUser)
                && !ObjectUtils.isEmpty(verifyCodeSession)
                && verifyCodeSession.equalsIgnoreCase(verifyCodeUser)){
            //4.进行身份认证
            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,password);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
        //5.如果验证码不匹配 抛出异常
        throw new LonginVerifyCodeException("验证不正确！");
    }
}
