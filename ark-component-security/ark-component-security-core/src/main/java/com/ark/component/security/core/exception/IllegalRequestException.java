package com.ark.component.security.core.exception;

import org.springframework.security.core.AuthenticationException;

public class IllegalRequestException extends AuthenticationException {

    public IllegalRequestException(String msg) {
        super(msg);
    }

}
