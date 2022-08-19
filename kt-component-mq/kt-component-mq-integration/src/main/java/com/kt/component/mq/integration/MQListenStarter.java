package com.kt.component.mq.integration;


import cn.hutool.core.map.MapUtil;
import com.kt.component.mq.configuation.MQConfiguration;
import com.kt.component.mq.core.annotations.MQMessageListener;
import com.kt.component.mq.core.exception.MQListenException;
import com.kt.component.mq.core.listener.MQListener;
import com.kt.component.mq.core.listener.MQListenerConfig;
import com.kt.component.mq.core.processor.MQMessageProcessor;
import com.kt.component.mq.core.support.MQ;
import com.kt.component.mq.rabbit.listener.RabbitMQListener;
import com.kt.component.mq.rocket.listener.RocketMQListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Slf4j
public class MQListenStarter implements ApplicationRunner, ApplicationContextAware {

    private final Map<MQ, MQListener> CACHE = new ConcurrentHashMap<>(6);

    private final MQConfiguration mqConfiguration;
    private ApplicationContext applicationContext;

    public MQListenStarter(MQConfiguration mqConfiguration) {
        this.mqConfiguration = mqConfiguration;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, MQMessageProcessor> processorMap = applicationContext.getBeansOfType(MQMessageProcessor.class);
        processorMap.forEach((key, processor) -> doListen(processor));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void doListen(MQMessageProcessor processor) {
        MQMessageListener annotation = processor.getClass().getAnnotation(MQMessageListener.class);
        if (annotation != null) {
            MQListenerConfig config = buildConfig(annotation);
            MQ mq = config.getMq();
            MQListener mqListener = getMqInstance(mq);
            try {
                mqListener.listen(processor, mqConfiguration, config);
                log.info("[mq lister] processor: [{}] listen success", processor.getClass().getName());
            } catch (Exception e) {
                log.error("[mq lister] processor: [{}] listen error", processor.getClass().getName());
                throw new MQListenException(e);
            }
        }
    }

    private MQListener getMqInstance(MQ mq) {
        MQListener listener;
        switch (mq) {
            case ROCKET:
                listener = getAndCache(MQ.ROCKET, RocketMQListener.class, RocketMQListener::new);
                break;
            case RABBIT:
                listener = getAndCache(MQ.RABBIT, RabbitMQListener.class, RabbitMQListener::new);
                break;
            case PULSAR:
            case KAFKA:
            default:
                throw new MQListenException("尚未实现[" + mq.name() + "]MQ");
        }
        return listener;
    }

    private <T extends MQListener> MQListener getAndCache(MQ mq,
                                                          Class<? extends MQListener> clazz,
                                                          Supplier<T> supplier) {
        MQListener listener = MapUtil.get(CACHE, mq, clazz);
        if (Objects.isNull(listener)) {
            listener = supplier.get();
            CACHE.put(mq, listener);
        }
        return listener;
    }

    private MQListenerConfig buildConfig(MQMessageListener annotation) {
        MQListenerConfig mQListenerConfig = new MQListenerConfig();
        mQListenerConfig.setConsumeTimeout(annotation.consumeTimeout());
        mQListenerConfig.setConsumerGroup(annotation.consumerGroup());
        mQListenerConfig.setTopic(annotation.topic());
        mQListenerConfig.setTag(annotation.tags());
        mQListenerConfig.setConsumeMode(annotation.consumeMode());
        mQListenerConfig.setMq(annotation.mq());
        return mQListenerConfig;
    }
}
