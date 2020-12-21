package com.kt.component.logger;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kt.component.exception.BaseException;
import com.kt.component.exception.BizException;
import com.kt.component.exception.SysException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationPart;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @ Author        :  JavisChen
 */
@Aspect
@Slf4j
public class CatchLogAspect {

    /**
     * The syntax of pointcut : https://blog.csdn.net/zhengchao1991/article/details/53391244
     */
    @Pointcut("(@within(com.kt.component.logger.CatchAndLog) && execution(public * *(..))) " +
            "|| @annotation(com.kt.component.logger.CatchAndLog) && execution(public * *(..))")
    public void pointcut() {

    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String sn = StrUtil.uuid();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        logRequest(joinPoint, request, sn);

        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            logResponse(startTime, result, response, sn);
        }

        return result;
    }

    private void logResponse(long startTime, Object response, HttpServletResponse httpServletResponse, String sn) {
        long endTime = System.currentTimeMillis();
        try {
            log.debug("[resp] response : " + JSON.toJSONString(response));
            log.debug("[resp] cost : " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            log.error("logResponse error : " + e);
        } finally {
            log.debug("========================= REQUEST FINISHED {} =========================", sn);
        }
    }

    private void logRequest(ProceedingJoinPoint joinPoint, HttpServletRequest request, String sn) {
        try {
            log.debug("========================= REQUEST PROCESSING {} =========================", sn);
            log.debug("[req] url : " + request.getRequestURL().toString());
            log.debug("[req] remote_host : " + request.getRemoteHost());
            log.debug("[req] http_method: " + request.getMethod());
            log.debug("[req] content_type: " + request.getContentType());
            logArgs(request, joinPoint);
            log.debug("[req] native_method: " + joinPoint.getSignature().toString());
        } catch (Exception e) {
            log.error("[req] log request error : " + e);
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
                log.debug("[req] json args: " + JSON.toJSONString(args[0]));

            }
        } else if (isMatchMediaType(contentType, MediaType.MULTIPART_FORM_DATA_VALUE)) {
            for (Object arg : args) {
                if (arg != null) {
                    if (LinkedList.class.isAssignableFrom(arg.getClass())) {
                        LinkedList<?> linkedList = (LinkedList<?>) arg;
                        linkedList.forEach(item -> {
                            MultipartFile file = (MultipartFile) item;
                            log.debug("[req] file name: " + file.getOriginalFilename());
                            log.debug("[req] file size: " + file.getSize());
                        });
                    }
                }
            }
        } else {
            log.debug("[req] queryString args: " + request.getQueryString());
        }
    }

    private boolean isMatchMediaType(String contentType, String applicationJsonValue) {
        return contentType.contains(applicationJsonValue);
    }


}
