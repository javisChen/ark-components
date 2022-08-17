/*
 * *
 *  @(#) ${NAME}.java 1.0 ${YEAR}-${MONTH}-${DAY}
 *  Copyright (c) ${YEAR}, YUNXI. All rights reserved.
 *  YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * /
 */

package com.kt.biz.log.core.aspect;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.kt.biz.log.context.BizLogContext;
import com.kt.biz.log.context.annotation.BizLogRecord;
import com.kt.biz.log.parse.ParseException;
import com.kt.biz.log.parse.parser.TemplateParser;
import com.kt.biz.log.parse.support.IOperatorService;
import com.kt.biz.log.parse.support.Operator;
import com.kt.biz.log.persistence.BizLogInfo;
import com.kt.biz.log.persistence.BizLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Aspect
@Slf4j
public class BizLogRecordAspect {

    private final TemplateParser templateParser;
    private BizLogRepository bizLogRepository;
    private ExecutorService threadPool;
    /**
     * 是否异步记录日志，默认为false
     */
    private boolean async = false;

    private final IOperatorService operatorService;

    public BizLogRecordAspect(boolean async,
                              TemplateParser templateParser,
                              IOperatorService operatorService) {
        this.templateParser = templateParser;
        this.async = async;
        this.operatorService = operatorService;
    }

    public BizLogRecordAspect(boolean async,
                              TemplateParser templateParser,
                              BizLogRepository bizLogRepository,
                              IOperatorService operatorService) {
        this.templateParser = templateParser;
        this.bizLogRepository = bizLogRepository;
        this.async = async;
        this.operatorService = operatorService;
        if (this.async) {
            threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
    }

    @Pointcut("@annotation(com.kt.biz.log.context.annotation.BizLogRecord)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
        Object proceed;
        try {
            proceed = point.proceed();
            BizLogInfo.BizLogInfoBuilder logBuilder = createBizLogInfo();
            if (async) {
                threadPool.execute(() -> processBizLog(point, logBuilder));
            } else {
                processBizLog(point, logBuilder);
            }
        } finally {
            BizLogContext.clear();
        }
        return proceed;
    }

    private BizLogInfo.BizLogInfoBuilder createBizLogInfo() {
        HashMap<String, Object> variables = new HashMap<>(16);
        Operator operator = operatorService.get();
        String operatorUsername = operator.getUsername();
        variables.put("operator", operatorUsername);
        variables.put("operatorId", operator.getId());
        Map<String, Object> contextVariables = BizLogContext.getVariables();
        if (contextVariables != null && !contextVariables.isEmpty()) {
            variables.putAll(contextVariables);
        }
        return BizLogInfo.builder()
                .variables(variables)
                .clientType(getClientType())
                .createPerson(operatorUsername)
                .createTime(LocalDateTime.now());
    }

    private void processBizLog(ProceedingJoinPoint point, BizLogInfo.BizLogInfoBuilder builder) {
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            BizLogRecord annotation = signature.getMethod().getAnnotation(BizLogRecord.class);
            Object[] args = point.getArgs();
            Map<String, Object> variables = builder.build().getVariables();
            String[] parameterNames = signature.getParameterNames();
            for (int i = 0; i < parameterNames.length; i++) {
                variables.put(parameterNames[i], args[i]);
            }
            String condition = annotation.condition();
            boolean passCondition = parseCondition(variables, condition);
            if (passCondition) {
                BizLogInfo bizLogInfo = buildBizLoginInfo(builder, annotation, variables);
                bizLogInfo.setVariables(variables);
                if (bizLogRepository != null) {
                    bizLogRepository.save(bizLogInfo);
                }
            }
        } catch (Exception e) {
            log.error("记录操作日志异常", e);
        }
    }

    private boolean parseCondition(Map<String, Object> variables, String condition) {
        if (!StringUtils.hasText(condition)) {
            return true;
        }
        try {
            return Boolean.parseBoolean(templateParser.parse(condition, variables));
        } catch (ParseException e) {
            throw new ParseException("解析[condition]失败，检查设定值是否Boolean类型");
        }
    }

    private BizLogInfo buildBizLoginInfo(BizLogInfo.BizLogInfoBuilder builder,
                                         BizLogRecord annotation,
                                         Map<String, Object> variables) {
        String bizId = annotation.bizId();
        String template = annotation.success();
        String content = templateParser.parse(template, variables);
        String nodeText = annotation.nodeText();
        String extension = annotation.extension();
        String type = annotation.type();
        String prefix = "#{";
        String parseNodeText = nodeText.contains(prefix) ? templateParser.parse(nodeText, variables) : nodeText;
        String parseType = type.contains(prefix) ? templateParser.parse(type, variables) : type;
        String parseExtension = extension.contains(prefix) ? templateParser.parse(extension, variables) : extension;
        log.info("[业务操作日志] -> 解析完成 [类型=[{}], 记录详情=[{}]]", parseNodeText, content);
        BizLogInfo build = builder.content(content)
                .extension(parseExtension)
                .nodeText(parseNodeText)
                .type(parseType)
                .fileFieldName(annotation.fileFieldName())
                .fileType(annotation.fileType())
                .build();
        if (StringUtils.hasLength(bizId)) {
            String parse = templateParser.parse(bizId, variables);
            if (!StringUtils.hasLength(parse)) {
                throw new ParseException("bizId解析为空");
            }
            build.setBizId(Long.valueOf(parse));
        }
        return build;
    }

    private String getClientType() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            String header = (((ServletRequestAttributes) requestAttributes)).getRequest().getHeader("user-agent");
            UserAgent parse = UserAgentUtil.parse(header);
            return parse.isMobile() ? "APP" : "PC";
        }
        return "-";
    }


}
