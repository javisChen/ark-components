package com.ark.component.mq.exception;

/**
 * 丢弃消息
 */
public class MessageDiscardException extends MQException {

    public MessageDiscardException() {
    }

    public MessageDiscardException(String message) {
        super(message);
    }

    public MessageDiscardException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageDiscardException(Throwable cause) {
        super(cause);
    }

    public MessageDiscardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
