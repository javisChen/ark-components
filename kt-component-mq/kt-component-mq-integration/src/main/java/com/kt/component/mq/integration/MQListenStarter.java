package com.kt.component.mq.integration;


import com.kt.component.mq.core.annotations.MQMessageListener;
import com.kt.component.mq.core.exception.MQListenException;
import com.kt.component.mq.core.listener.MQListener;
import com.kt.component.mq.core.listener.MQListenerConfig;
import com.kt.component.mq.core.processor.MQMessageProcessor;
import com.kt.component.mq.core.support.MQType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MQListenStarter implements ApplicationRunner, ApplicationContextAware {

    private Map<MQType, MQListener> listenerHolder;

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
        Map<String, MQListener> map = applicationContext.getBeansOfType(MQListener.class);
        listenerHolder = new HashMap<>(map.size());
        map.forEach((key, listener) -> listenerHolder.put(listener.mqType(), listener));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void doListen(MQMessageProcessor processor) {
        MQMessageListener annotation = processor.getClass().getAnnotation(MQMessageListener.class);
        if (annotation != null) {
            MQListenerConfig config = buildConfig(annotation);
            MQListener mqListener = listenerHolder.get(config.getMqType());
            try {
                mqListener.listen(processor, config);
                log.info("[mq listen] processor: [{}] listen success", processor.getClass().getName());
            } catch (Exception e) {
                log.error("[mq listen] processor: [{}] listen error", processor.getClass().getName());
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
}
