package com.ganga.security.exception;

import org.springframework.security.core.AuthenticationException;

public class LoginVerifyCodeException extends AuthenticationException {
    public LoginVerifyCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public LoginVerifyCodeException(String msg) {
        super(msg);
    }
}
