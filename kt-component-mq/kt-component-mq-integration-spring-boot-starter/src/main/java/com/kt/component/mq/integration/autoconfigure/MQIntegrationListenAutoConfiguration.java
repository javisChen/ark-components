package com.kt.component.mq.integration.autoconfigure;

import com.kt.component.mq.core.generator.DefaultMessageIdGenerator;
import com.kt.component.mq.core.generator.MessageIdGenerator;
import com.kt.component.mq.core.serializer.FastJSONCodec;
import com.kt.component.mq.core.serializer.MessageCodec;
import com.kt.component.mq.integration.MQListenStarter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * MQ集成监听装配类
 */
@Slf4j
public class MQIntegrationListenAutoConfiguration {

    public MQIntegrationListenAutoConfiguration() {
        log.info("enable [kt-component-mq-integration-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnMissingBean
    public MQListenStarter mqListenStarter() {
        return new MQListenStarter();
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageCodec messageCodec() {
        return new FastJSONCodec();
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageIdGenerator messageIdGenerator() {
        return new DefaultMessageIdGenerator();
    }

}
