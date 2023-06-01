package com.ark.component.microservice.rpc.exception;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ark.component.exception.ExceptionFactory;
import com.ark.component.exception.RpcException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FeignCommonErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        Response.Body body = response.body();
        String bodyString = readStrFromBody(body);
        RpcException rpcException;
        if (status == HttpStatus.UNAUTHORIZED.value()) {
            JSONObject jsonBody = JSONObject.parseObject(bodyString);
            String msg = getMsgFromBody(jsonBody);
            String service = getServiceFromBody(jsonBody);
            String code = getCodeFromBody(jsonBody);
            rpcException = ExceptionFactory.rpcException(service, response, msg, code);
        } else if (status == HttpStatus.FORBIDDEN.value()) {
            JSONObject jsonBody = JSONObject.parseObject(bodyString);
            String msg = getMsgFromBody(jsonBody);
            String service = getServiceFromBody(jsonBody);
            String code = getCodeFromBody(jsonBody);
            rpcException = ExceptionFactory.rpcException(service, response, msg, code);
        } else {
            // 503：直接返回body的提示语即可
            rpcException = new RpcException(null, response, bodyString, null);
        }
        // 这样直接throw才会被统一异常捕捉
        throw rpcException;
    }

    private String getCodeFromBody(JSONObject body) {
        return body.getString("code");
    }

    private String getMsgFromBody(JSONObject body) {
        return body.getString("msg");
    }

    private String getServiceFromBody(JSONObject body) {
        return body.getString("service");
    }

    private String readStrFromBody(Response.Body body) {
        String read = "";
        try {
            read = IoUtil.read(body.asInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("读取输入流异常", e);
        }
        return read;
    }
}
