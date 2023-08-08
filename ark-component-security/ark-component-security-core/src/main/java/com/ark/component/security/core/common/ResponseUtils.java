package com.ark.component.security.core.common;

import com.alibaba.fastjson2.JSON;
import com.ark.component.dto.ServerResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResponseUtils {

    public static void write(ServerResponse serverResponse, HttpServletResponse response, int status) throws IOException {
        byte[] body = JSON.toJSONBytes(serverResponse);
        response.setStatus(status);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        response.setContentLength(body.length);
        response.getOutputStream().write(body);
    }
}
