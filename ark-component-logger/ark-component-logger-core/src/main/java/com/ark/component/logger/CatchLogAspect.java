package com.ark.component.logger;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.LinkedList;

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
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return joinPoint.proceed();
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        HttpServletRequest request = requestAttributes.getRequest();
        logRequest(joinPoint, request);

        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            stopWatch.stop();
            logResponse(stopWatch, result);
        }

        return result;
    }

    private void logRequest(ProceedingJoinPoint joinPoint, HttpServletRequest request) {
        try {
            log.info("========================= Request Begin =========================");
            log.info("Uri: " + request.getRequestURI());
            log.info("RemoteHost: " + request.getRemoteHost());
            log.info("HttpMethod: " + request.getMethod());
            log.info("Signature: " + joinPoint.getSignature().toString());
            log.info("============================ Headers ============================");
            logHeaders(request);
            log.info("============================ Headers ============================");
            logArgs(request, joinPoint);
        } catch (Exception e) {
            log.error("Log Request Error", e);
        }
    }

    private void logResponse(StopWatch stopWatch, Object response) {
        try {
            log.info("Response : " + JSON.toJSONString(response));
            log.info("Cost : " + stopWatch.getTotalTimeMillis() + " ms");
        } catch (Exception e) {
            log.error("Log Response Error", e);
        } finally {
            log.info("========================= Request End =========================");
        }
    }

    public void logHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headerValues = request.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                String headerValue = headerValues.nextElement();
                log.info("{}: {}", headerName, headerValue);
            }
        }
    }

    private void logArgs(HttpServletRequest request, ProceedingJoinPoint joinPoint) {

        String contentType = request.getContentType();
        Object[] args = joinPoint.getArgs();
        if (contentType == null) {
            log.info("QueryString Args: " + request.getQueryString());
            return;
        }
        if (isMatchMediaType(contentType, MediaType.APPLICATION_JSON_VALUE)) {
            if (args.length > 0) {
                log.info("Json Args: " + JSON.toJSONString(args[0]));
            }
        } else if (isMatchMediaType(contentType, MediaType.MULTIPART_FORM_DATA_VALUE)) {
            for (Object arg : args) {
                if (arg != null) {
                    if (LinkedList.class.isAssignableFrom(arg.getClass())) {
                        LinkedList<?> linkedList = (LinkedList<?>) arg;
                        linkedList.forEach(item -> {
                            MultipartFile file = (MultipartFile) item;
                            log.info("File name: " + file.getOriginalFilename());
                            log.info("File size: " + file.getSize());
                        });
                    }
                }
            }
        } else {
            log.info("QueryString Args: " + request.getQueryString());
        }
    }

    private boolean isMatchMediaType(String contentType, String applicationJsonValue) {
        return contentType.contains(applicationJsonValue);
    }

}
