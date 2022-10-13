package com.ark.biz.log.core;

public class BizLogException extends RuntimeException {

    public BizLogException() {
    }

    public BizLogException(String message) {
        super(message);
    }

    public BizLogException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizLogException(Throwable cause) {
        super(cause);
    }

    public BizLogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
