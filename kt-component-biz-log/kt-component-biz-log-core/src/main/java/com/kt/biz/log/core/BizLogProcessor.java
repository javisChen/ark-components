package com.kt.biz.log.core;

import com.kt.biz.log.context.BizLogContext;
import com.kt.biz.log.context.annotation.BizLogRecord;
import com.kt.biz.log.core.support.BizLogKit;
import com.kt.biz.log.parse.ParseException;
import com.kt.biz.log.parse.parser.TemplateParser;
import com.kt.biz.log.parse.support.IOperatorService;
import com.kt.biz.log.parse.support.Operator;
import com.kt.biz.log.persistence.BizLogInfo;
import com.kt.biz.log.persistence.BizLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class BizLogProcessor implements InitializingBean {

    /**
     * 是否异步记录日志，默认为false
     */
    private boolean async = false;

    private final IOperatorService operatorService;

    private final TemplateParser templateParser;
    private BizLogRepository bizLogRepository;

    /**
     * 加上最大容量限制，防止OOM
     */
    private final LinkedBlockingQueue<BizLogTask> taskQueue = new LinkedBlockingQueue<>(500);


    public BizLogProcessor(boolean async,
                           TemplateParser templateParser,
                           IOperatorService operatorService) {
        this.templateParser = templateParser;
        this.async = async;
        this.operatorService = operatorService;
    }

    public BizLogProcessor(boolean async,
                           TemplateParser templateParser,
                           BizLogRepository bizLogRepository,
                           IOperatorService operatorService) {
        this.templateParser = templateParser;
        this.bizLogRepository = bizLogRepository;
        this.async = async;
        this.operatorService = operatorService;
        if (this.async) {
            ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
    }

    public void enqueue(ProceedingJoinPoint point, BizLogInfo.BizLogInfoBuilder bizLogInfo) {
        try {
            taskQueue.put(new BizLogTask(point, bizLogInfo));
        } catch (InterruptedException e) {
            log.error("[BIZ LOG] 线程中断异常", e);
        }
    }

    public void process(ProceedingJoinPoint point) {
        try {
            BizLogInfo.BizLogInfoBuilder logBuilder = prepareBuilder();
            if (async) {
                enqueue(point, logBuilder);
            } else {
                doProcess(point, logBuilder);
            }
        } finally {
            BizLogContext.clear();
        }
    }

    private BizLogInfo.BizLogInfoBuilder prepareBuilder() {
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
                .clientType(BizLogKit.getClientType())
                .createPerson(operatorUsername)
//                .createPersonId(ServiceContext.getContext().getRequestUserId())
                .createTime(LocalDateTime.now())
//                .instanceId(ServiceContext.getContext().getRequestInstanceId())
//                .tenantId(ServiceContext.getContext().getRequestTenantId());
        ;
    }

    private void doProcess(ProceedingJoinPoint point, BizLogInfo.BizLogInfoBuilder builder) {
        try {
            Object[] args = point.getArgs();
            Map<String, Object> variables = builder.build().getVariables();
            MethodSignature signature = (MethodSignature) point.getSignature();
            String[] parameterNames = signature.getParameterNames();
            for (int i = 0; i < parameterNames.length; i++) {
                variables.put(parameterNames[i], args[i]);
            }
            BizLogRecord annotation = signature.getMethod().getAnnotation(BizLogRecord.class);
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
            if (!async) {
                throw new BizLogException(e);
            }
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

    @Override
    public void afterPropertiesSet() {
        startProcessThread();
    }

    private void startProcessThread() {
        new Thread(() -> {
            while (true) {
                try {
                    BizLogTask bizLogTask = taskQueue.take();
                    doProcess(bizLogTask.getPoint(), bizLogTask.getBuilder());
                } catch (InterruptedException e) {
                    log.error("[BIZ LOG] 线程中断异常", e);
                }
            }
        }).start();
        log.info("[BIZ LOG] 启动日志处理线程");
    }

}
