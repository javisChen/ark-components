package com.kt.component.microservice.rpc.exception;

/**
 * RPC调用异常
 * @author javischen
 */
public class RpcException extends RuntimeException {

    private String service;

    public RpcException() {

    }

    public RpcException(String service, String message) {
        super(message);
        this.service = service;
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getService() {
        return service;
    }
}
