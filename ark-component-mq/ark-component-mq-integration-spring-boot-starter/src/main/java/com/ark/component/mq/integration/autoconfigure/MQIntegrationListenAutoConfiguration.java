package com.ark.component.mq.integration.autoconfigure;

import com.ark.component.mq.core.generator.DefaultMsgIdGenerator;
import com.ark.component.mq.core.generator.MsgIdGenerator;
import com.ark.component.mq.core.serializer.FastJSONSerializer;
import com.ark.component.mq.core.serializer.MessageSerializer;
import com.ark.component.mq.integration.MessageListenRegister;
import com.ark.component.mq.integration.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * MQ集成监听装配类
 */
@Slf4j
public class MQIntegrationListenAutoConfiguration {

    public MQIntegrationListenAutoConfiguration() {
        log.info("enable [ark-component-mq-integration-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageListenRegister mqListenStarter() {
        return new MessageListenRegister();
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageSerializer messageSerializer() {
        return new FastJSONSerializer();
    }

    @Bean
    @ConditionalOnMissingBean
    public MsgIdGenerator messageIdGenerator() {
        return new DefaultMsgIdGenerator();
    }
    @Bean
    @ConditionalOnMissingBean
    public MessageTemplate messageTemplate() {
        return new MessageTemplate();
    }

}
