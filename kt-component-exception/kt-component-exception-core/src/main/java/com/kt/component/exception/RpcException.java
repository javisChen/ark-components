package com.kt.component.exception;

/**
 * RPC调用异常
 * @author javischen
 */
public class RpcException extends RuntimeException {

    private String service;
    private String bizErrorCode;
    private Object response;

    public RpcException() {

    }

    public RpcException(String service, Object response, String message, String bizErrorCode) {
        super(message);
        this.service = service;
        this.response = response;
        this.bizErrorCode = bizErrorCode;
    }

    public RpcException(String service, String message, String bizErrorCode) {
        super(message);
        this.service = service;
        this.bizErrorCode = bizErrorCode;
    }


    public RpcException(String service, String message) {
        super(message);
        this.service = service;
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getService() {
        return service;
    }

    public Object getResponse() {
        return response;
    }

    public String getBizErrorCode() {
        return bizErrorCode;
    }
}
