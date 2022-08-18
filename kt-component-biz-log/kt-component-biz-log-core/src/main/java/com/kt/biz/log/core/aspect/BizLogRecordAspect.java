package com.kt.biz.log.core.aspect;

import com.kt.biz.log.context.BizLogContext;
import com.kt.biz.log.core.BizLogProcessor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class BizLogRecordAspect {

    private final BizLogProcessor processor;

    public BizLogRecordAspect(BizLogProcessor processor) {
        this.processor = processor;
    }

    @Pointcut("@annotation(com.kt.biz.log.context.annotation.BizLogRecord)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
        Object proceed;
        // 如果执行业务过程报错，需要主动把BizLogContext清除，防止内存泄露
        try {
            proceed = point.proceed();
        } catch (Throwable e) {
            BizLogContext.clear();
            throw e;
        }
        processor.process(point);
        return proceed;
    }


}
