package com.ganga.security;

import org.springframework.core.log.LogMessage;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

public class RememberMeServiceImpl extends PersistentTokenBasedRememberMeServices {

    /**
     * 必须构建super构造器
     * @param key
     * @param userDetailsService
     * @param tokenRepository
     */
    public RememberMeServiceImpl(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    /**
     * 重写父类的父类 中 的 rememberMeRequested()
     *
     * @param request
     * @param parameter
     * @return
     */
    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {

        //先经过 LoginVerifyCodeFilter 认证 从json中取出并封装后 有放入了session中
        String paramValue = (String) request.getSession().getAttribute(DEFAULT_PARAMETER);

        //提取父类的
        if (paramValue != null) {
            if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
                    || paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
                return true;
            }
        }
        this.logger.debug(
                LogMessage.format("Did not send remember-me cookie (principal did not set parameter '%s')", parameter));
        return false;
    }
}
