package com.ark.component.mq.exception;

/**
 * MQ解编码异常
 * @author victor
 */
public class MQCodecException extends RuntimeException {

    public MQCodecException() {
    }

    public MQCodecException(String message) {
        super(message);
    }

    public MQCodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQCodecException(Throwable cause) {
        super(cause);
    }

    public MQCodecException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
