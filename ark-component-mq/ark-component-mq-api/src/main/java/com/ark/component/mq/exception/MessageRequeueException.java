package com.ark.component.mq.exception;

/**
 * 消息重新入列
 */
public class MessageRequeueException extends MQException {

    public MessageRequeueException() {
    }

    public MessageRequeueException(String message) {
        super(message);
    }

    public MessageRequeueException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageRequeueException(Throwable cause) {
        super(cause);
    }

    public MessageRequeueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
