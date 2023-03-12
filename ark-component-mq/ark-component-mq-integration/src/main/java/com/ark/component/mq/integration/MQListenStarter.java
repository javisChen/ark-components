package com.ark.component.mq.integration;


import cn.hutool.core.map.MapUtil;
import com.ark.component.mq.core.annotations.MQMessageListener;
import com.ark.component.mq.exception.MQListenException;
import com.ark.component.mq.core.listener.MQListener;
import com.ark.component.mq.core.listener.MQListenerConfig;
import com.ark.component.mq.core.processor.MQMessageProcessor;
import com.ark.component.mq.core.support.MQType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SuppressWarnings("all")
public class MQListenStarter implements ApplicationRunner, ApplicationContextAware {

    private Map<MQType, MQListener<?>> listenerHolder;

    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) {

        prepareListeners();

        startListen();

    }

    private void startListen() {
        Map<String, MQMessageProcessor> processorMap = applicationContext.getBeansOfType(MQMessageProcessor.class);
        processorMap.forEach((key, processor) -> doListen(processor));
    }

    private void prepareListeners() {
        Map<String, MQListener> listenerMap = applicationContext.getBeansOfType(MQListener.class);
        if (MapUtil.isEmpty(listenerMap)) {
            return;
        }
        listenerHolder = new HashMap<>(listenerMap.size());
        for (Map.Entry<String, MQListener> entry : listenerMap.entrySet()) {
            MQListener listener = entry.getValue();
            listenerHolder.put(listener.getMqType(), listener);
        }
    }

    public void doListen(MQMessageProcessor processor) {
        if (MapUtil.isEmpty(listenerHolder)) {
            return;
        }
        MQMessageListener annotation = processor.getClass().getAnnotation(MQMessageListener.class);
        if (annotation != null) {
            MQListenerConfig config = buildConfig(annotation);
            MQListener mqListener = listenerHolder.get(config.getMqType());
            String processClazzName = processor.getClass().getName();
            try {
                mqListener.listen(processor, config);
                log.info("[MQ] Consumer listen successfully,Processor = [{}],Config = [{}]", processClazzName, config);
            } catch (Exception e) {
                log.error("[MQ] Consumer failed to listen,[" + processClazzName + "] listen error, config:[" + config + "]", e);
                throw new MQListenException(e);
            }
        }
    }

    private MQListenerConfig buildConfig(MQMessageListener annotation) {
        MQListenerConfig mQListenerConfig = new MQListenerConfig();
        mQListenerConfig.setConsumeTimeout(annotation.consumeTimeout());
        mQListenerConfig.setConsumerGroup(annotation.consumerGroup());
        mQListenerConfig.setTopic(annotation.topic());
        mQListenerConfig.setTag(annotation.tags());
        mQListenerConfig.setConsumeMode(annotation.consumeMode());
        mQListenerConfig.setMqType(annotation.mq());
        return mQListenerConfig;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
