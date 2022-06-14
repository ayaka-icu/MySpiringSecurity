package com.ganga.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ganga.security.exception.LoginVerifyCodeException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginVerifyCodeFilter extends UsernamePasswordAuthenticationFilter {

    public static final String FORM_VERIFY_CODE_KEY = "verifyCode";

    private String verifyCodeKey = FORM_VERIFY_CODE_KEY;

    public String getVerifyCodeKey() {
        return verifyCodeKey;
    }

    public void setVerifyCodeKey(String verifyCodeKey) {
        this.verifyCodeKey = verifyCodeKey;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            //1.获取用户输入信息
            Map<String, String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            String username = userInfo.get(getUsernameParameter());
            String password = userInfo.get(getPasswordParameter());
            String verifyUser = userInfo.get(getVerifyCodeKey());
            //2.从session中获取验证码
            String verifySession = (String) request.getSession().getAttribute("verifyCode");
            //3.验证验证码是否正确
            if (!ObjectUtils.isEmpty(verifyUser)
                    && !ObjectUtils.isEmpty(verifySession)
                    && verifySession.equalsIgnoreCase(verifyUser)) {
                //4.进行身份认证
                UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
                setDetails(request, authRequest);
                //5.返回认证结果
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //6.如果验证码没用通过，抛出异常
        throw new LoginVerifyCodeException("验证码不匹配！");
    }
}
