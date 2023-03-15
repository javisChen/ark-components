package com.ark.component.mq.integration;


import cn.hutool.core.map.MapUtil;
import com.ark.component.mq.core.annotations.MQMessageListener;
import com.ark.component.mq.exception.MQListenException;
import com.ark.component.mq.core.listener.MQListener;
import com.ark.component.mq.core.listener.MQListenerConfig;
import com.ark.component.mq.core.processor.MessageHandler;
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
public class MessageListenRegistrar implements ApplicationRunner, ApplicationContextAware {

    private Map<MQType, MQListener<?>> listenerHolder;

    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) {

        prepareListeners();

        start();

    }

    private void start() {
        Map<String, MessageHandler> processorMap = applicationContext.getBeansOfType(MessageHandler.class);
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

    public void doListen(MessageHandler handler) {
        if (MapUtil.isEmpty(listenerHolder)) {
            return;
        }
        MQMessageListener annotation = handler.getClass().getAnnotation(MQMessageListener.class);
        if (annotation != null) {
            MQListenerConfig config = buildConfig(annotation);
            MQListener mqListener = listenerHolder.get(config.getMqType());
            String handleClazzName = handler.getClass().getName();
            try {
                mqListener.listen(handler, config);
                log.info("[MQ] Consumer listen successfully,Handler = [{}],Config = [{}]", handleClazzName, config);
            } catch (Exception e) {
                log.error("[MQ] Consumer failed to listen,[" + handleClazzName + "] listen error, config:[" + config + "]", e);
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
