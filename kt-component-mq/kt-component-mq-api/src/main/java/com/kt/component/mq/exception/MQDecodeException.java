package com.kt.component.mq.exception;

/**
 * MQ解编码异常
 * @author victor
 */
public class MQDecodeException extends RuntimeException {

    public MQDecodeException() {
    }

    public MQDecodeException(String message) {
        super(message);
    }

    public MQDecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQDecodeException(Throwable cause) {
        super(cause);
    }

    public MQDecodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
