package com.ark.component.logger;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedList;

/**
 * @ Author:  JavisChen
 */
@Aspect
@Slf4j
public class CatchLogAspect {

    /**
     * The syntax of pointcut : https://blog.csdn.net/zhengchao1991/article/details/53391244
     */
    @Pointcut("( within(com.ark.component.web.base.BaseController+))" +
            "||@within(com.ark.component.logger.annotation.CatchAndLog)" +
            "|| @annotation(com.ark.component.logger.annotation.CatchAndLog)")
    public void pointcut() {

    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        logRequest(joinPoint, request);

        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            logResponse(startTime, result);
        }

        return result;
    }

    private void logResponse(long startTime, Object response) {
        long endTime = System.currentTimeMillis();
        try {
            log.info("[resp] response : " + JSON.toJSONString(response));
            log.info("[resp] cost : " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            log.error("logResponse error : " + e);
        } finally {
            log.info("========================= REQUEST FINISHED =========================");
        }
    }

    private void logRequest(ProceedingJoinPoint joinPoint, HttpServletRequest request) {
        try {
            log.info("========================= REQUEST PROCESSING =========================");
            log.info("url : " + request.getRequestURL().toString());
            log.info("remote_host : " + request.getRemoteHost());
            log.info("http_method: " + request.getMethod());
            log.info("content_type: " + request.getContentType());
            log.info("user_agent : " + request.getHeader("User-Agent"));
            logArgs(request, joinPoint);
            log.info("native_method: " + joinPoint.getSignature().toString());
        } catch (Exception e) {
            log.error("log request error : " + e);
        }
    }

    private void logArgs(HttpServletRequest request, ProceedingJoinPoint joinPoint) {

        String contentType = request.getContentType();
        Object[] args = joinPoint.getArgs();
        if (contentType == null) {
            return;
        }
        if (isMatchMediaType(contentType, MediaType.APPLICATION_JSON_VALUE)) {
            if (args.length > 0) {
                log.info("json args: " + JSON.toJSONString(args[0]));
            }
        } else if (isMatchMediaType(contentType, MediaType.MULTIPART_FORM_DATA_VALUE)) {
            for (Object arg : args) {
                if (arg != null) {
                    if (LinkedList.class.isAssignableFrom(arg.getClass())) {
                        LinkedList<?> linkedList = (LinkedList<?>) arg;
                        linkedList.forEach(item -> {
                            MultipartFile file = (MultipartFile) item;
                            log.info("file name: " + file.getOriginalFilename());
                            log.info("file size: " + file.getSize());
                        });
                    }
                }
            }
        } else {
            log.info("queryString args: " + request.getQueryString());
        }
    }

    private boolean isMatchMediaType(String contentType, String applicationJsonValue) {
        return contentType.contains(applicationJsonValue);
    }

}
