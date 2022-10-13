package com.ark.component.statemachine.core.exception;

/**
 * 状态机处理异常类
 * @author victor
 */
public class ActionException extends RuntimeException {

    public ActionException(Throwable cause) {
        super(cause);
    }

    public ActionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ActionException(String message) {
        super(message);
    }

    public ActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
