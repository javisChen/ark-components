package com.kt.component.logger;

import com.kt.component.exception.BaseException;
import com.kt.component.exception.BizException;
import com.kt.component.exception.SysException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @ Author        :  JavisChen
 */
@Aspect
@Slf4j
public class CatchLogAspect {

    /**
     * The syntax of pointcut : https://blog.csdn.net/zhengchao1991/article/details/53391244
     * 在类上
     */
    @Pointcut("(@within(com.kt.component.logger.CatchAndLog) && execution(public * *(..))) " +
            "|| @annotation(com.kt.component.logger.CatchAndLog) && execution(public * *(..))")
    public void pointcut() {

    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        logRequest(joinPoint);

        Object response = null;
        try {
            response = joinPoint.proceed();
        } finally {
            logResponse(startTime, response);
        }

        return response;
    }

    private Object handleException(ProceedingJoinPoint joinPoint, Throwable e) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Class returnType = ms.getReturnType();

        if (e instanceof BizException) {
            log.warn("BIZ EXCEPTION : " + e.getMessage());
            //在Debug的时候，对于BizException也打印堆栈
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
            return ResponseHandler.handle(returnType, (BaseException) e);
        }

        if (e instanceof SysException) {
            log.error("SYS EXCEPTION :");
            log.error(e.getMessage(), e);
            return ResponseHandler.handle(returnType, (BaseException) e);
        }

        log.error("UNKNOWN EXCEPTION :");
        log.error(e.getMessage(), e);

        return ResponseHandler.handle(returnType, "UNKNOWN_ERROR", e.getMessage());
    }


    private void logResponse(long startTime, Object response) {
        try {
            long endTime = System.currentTimeMillis();
//            log.debug("RESPONSE : " + JSON.toJSONString(response));
            log.debug("COST : " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            //swallow it
            log.error("logResponse error : " + e);
        }
    }

    private void logRequest(ProceedingJoinPoint joinPoint) {
        try {
            log.debug("START PROCESSING: " + joinPoint.getSignature().toShortString());
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
//                log.debug("REQUEST : " + JSON.toJSONString(arg));
            }
        } catch (Exception e) {
            //swallow it
            log.error("logReqeust error : " + e);
        }
    }

}
