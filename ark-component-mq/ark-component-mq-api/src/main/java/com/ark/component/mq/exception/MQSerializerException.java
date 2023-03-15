package com.ark.component.mq.exception;

/**
 * MQ解编码异常
 * @author victor
 */
public class MQSerializerException extends MQException {

    public MQSerializerException() {
    }

    public MQSerializerException(String message) {
        super(message);
    }

    public MQSerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQSerializerException(Throwable cause) {
        super(cause);
    }

    public MQSerializerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
