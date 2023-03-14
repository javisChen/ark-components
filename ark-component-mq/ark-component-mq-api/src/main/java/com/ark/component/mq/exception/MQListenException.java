package com.ark.component.mq.exception;

/**
 * MQ注册消费监听异常
 */
public class MQListenException extends MQException {

    public MQListenException() {
    }

    public MQListenException(String message) {
        super(message);
    }

    public MQListenException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQListenException(Throwable cause) {
        super(cause);
    }

    public MQListenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
