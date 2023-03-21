//package com.ark.component.mq.kafka.autoconfigure;
//
//import com.ark.component.mq.rocket.RocketMQService;
//import com.ark.component.mq.rocket.configuation.RocketMQConfiguration;
//import com.ark.component.mq.rocket.listener.RocketMQListener;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//
//
///**
// * MQ集成监听装配类
// */
//@Slf4j
//@EnableConfigurationProperties(KafkaMQAutoConfiguration.class)
//public class KafkaMQAutoConfiguration {
//
//    public KafkaMQAutoConfiguration() {
//        log.info("enable [ark-component-mq-kafka-spring-boot-starter]");
//    }
//
//    @Bean
//    @ConditionalOnProperty(
//            prefix = "ark.component.mq.rocketmq",
//            value = "enabled",
//            havingValue = "true",
//            matchIfMissing = true)
//    @ConditionalOnMissingBean
//    public RocketMQListener rocketMQListener(RocketMQConfiguration configuration) {
//        return new RocketMQListener(configuration);
//    }
//
//    @Bean
//    @ConditionalOnProperty(
//            prefix = "ark.component.mq.rocketmq.producer",
//            value = "enabled",
//            havingValue = "true",
//            matchIfMissing = true)
//    @ConditionalOnMissingBean
//    public RocketMQService rocketMQService(RocketMQConfiguration configuration) {
//        return new RocketMQService(configuration);
//    }
//
////    @Bean
////    @ConditionalOnBean(RocketMQService.class)
////    @ConditionalOnMissingBean
////    public MessageProducer messageProducer(RocketMQService messageService) {
////        return new MessageProducer(messageService);
////    }
//
//}
