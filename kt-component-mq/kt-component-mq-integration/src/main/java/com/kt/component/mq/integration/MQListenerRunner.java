package com.kt.component.mq.integration;


import cn.hutool.core.map.MapUtil;
import com.kt.component.mq.configuation.MQConfiguration;
import com.kt.component.mq.core.support.MQ;
import com.kt.component.mq.core.listener.MQListener;
import com.kt.component.mq.core.listener.MQListenerConfig;
import com.kt.component.mq.core.annotations.MQMessageListener;
import com.kt.component.mq.core.processor.MQMessageProcessor;
import com.kt.component.mq.core.exception.MQListenException;
import com.kt.component.mq.rocket.RocketMQListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MQListenerRunner implements ApplicationRunner, ApplicationContextAware {

    private final Map<MQ, MQListener> CACHE = new ConcurrentHashMap<>(6);

    private final MQConfiguration mqConfiguration;
    private ApplicationContext applicationContext;

    public MQListenerRunner(MQConfiguration mqConfiguration) {
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
            mqListener.listen(processor, mqConfiguration, config);
        }
    }

    private MQListener getMqInstance(MQ mq) {
        MQListener clazz;
        switch (mq) {
            case ROCKET:
                clazz = MapUtil.get(CACHE, MQ.ROCKET, MQListener.class, new RocketMQListener());
                break;
            case RABBIT:
                throw new MQListenException("尚未实现[" + mq.name() + "]MQ");
            case PULSAR:
                throw new MQListenException("尚未实现[" + mq.name() + "]MQ");
            case KAFKA:
                throw new MQListenException("尚未实现[" + mq.name() + "]MQ");
            default:
                throw new MQListenException("尚未实现[" + mq.name() + "]MQ");
        }
        return clazz;
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
