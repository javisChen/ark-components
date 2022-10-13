package com.ark.component.openapi.http;


import com.ark.component.exception.BizException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpApiException extends BizException {

    public HttpApiException(String errMessage) {
        super(errMessage);
    }

    public HttpApiException(String errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public HttpApiException(String errMessage, Throwable e) {
        super(errMessage, e);
    }

    public HttpApiException(String errorCode, String errMessage, Throwable e) {
        super(errorCode, errMessage, e);
    }
}
