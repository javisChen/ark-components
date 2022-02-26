package com.kt.component.microservice.rpc.exception;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
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
        String bodyString = readFromBody(body);
        if (status == HttpStatus.UNAUTHORIZED.value()) {
            String msg = getMsgFromBody(bodyString);
            String service = getServiceFromBody(bodyString);
            String code = getCodeFromBody(bodyString);
            return new RpcException(service, response, msg, code);
        } else if (status == HttpStatus.FORBIDDEN.value()) {
            String msg = getMsgFromBody(bodyString);
            String service = getServiceFromBody(bodyString);
            String code = getCodeFromBody(bodyString);
            return new RpcException(service, response, msg, code);
        }
        return new RpcException(null, response, bodyString, null);
    }

    private String getCodeFromBody(String body) {
        return JSONObject.parseObject(body).getString("code");
    }

    private String getMsgFromBody(String body) {
        return JSONObject.parseObject(body).getString("msg");
    }

    private String getServiceFromBody(String body) {
        return JSONObject.parseObject(body).getString("service");
    }

    private String readFromBody(Response.Body body) {
        String read = "";
        try {
            read = IoUtil.read(body.asInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("读取输入流异常", e);
        }
        return read;
    }
}
