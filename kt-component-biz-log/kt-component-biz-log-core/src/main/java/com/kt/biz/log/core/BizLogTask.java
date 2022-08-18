package com.kt.biz.log.core;

import com.kt.biz.log.persistence.BizLogInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;

@AllArgsConstructor
@Getter
public class BizLogTask {

    private ProceedingJoinPoint point;

    private BizLogInfo.BizLogInfoBuilder builder;
}
