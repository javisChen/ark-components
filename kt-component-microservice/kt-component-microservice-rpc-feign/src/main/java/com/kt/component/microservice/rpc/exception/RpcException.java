package com.kt.component.microservice.rpc.exception;

import feign.Response;

/**
 * RPC调用异常
 * @author javischen
 */
public class RpcException extends RuntimeException {

    private String service;
    private String bizErrorCode;
    private Response response;

    public RpcException() {

    }

    public RpcException(String service, Response response, String message, String bizErrorCode) {
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

    public String getService() {
        return service;
    }

    public Response getResponse() {
        return response;
    }

    public String getBizErrorCode() {
        return bizErrorCode;
    }
}
