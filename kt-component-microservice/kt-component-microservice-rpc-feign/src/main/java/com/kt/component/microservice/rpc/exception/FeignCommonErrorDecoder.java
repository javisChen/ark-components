package com.kt.component.microservice.rpc.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignCommonErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        Response.Body body = response.body();
        return new RpcException(null, body.toString());
    }
}
