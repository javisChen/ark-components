package com.ark.component.statemachine.core.exception;

/**
 * 状态机处理异常类
 * @author victor
 */
public class GuardException extends RuntimeException {

    public GuardException(Throwable cause) {
        super(cause);
    }

    public GuardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GuardException(String message) {
        super(message);
    }

    public GuardException(String message, Throwable cause) {
        super(message, cause);
    }
}
