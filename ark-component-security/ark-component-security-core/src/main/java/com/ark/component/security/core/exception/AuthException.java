package com.ark.component.security.core.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthException extends AuthenticationException {

    private final int httpStatusCode;

    public AuthException(int httpStatusCode, String msg, Throwable cause) {
        super(msg, cause);
        this.httpStatusCode = httpStatusCode;
    }

    public static AuthException of(int httpStatusCode, String msg) {
        return new AuthException(httpStatusCode, msg, null);
    }
}
