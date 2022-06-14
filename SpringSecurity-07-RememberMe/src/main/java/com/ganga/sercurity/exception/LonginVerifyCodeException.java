package com.ganga.sercurity.exception;

import org.springframework.security.core.AuthenticationException;

public class LonginVerifyCodeException extends AuthenticationException {

    public LonginVerifyCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public LonginVerifyCodeException(String msg) {
        super(msg);
    }

}
