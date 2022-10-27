package com.ganga.security.exception;

import org.springframework.security.core.AuthenticationException;

public class VerifyCodeException extends AuthenticationException {

    public VerifyCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public VerifyCodeException(String msg) {
        super(msg);
    }

}
