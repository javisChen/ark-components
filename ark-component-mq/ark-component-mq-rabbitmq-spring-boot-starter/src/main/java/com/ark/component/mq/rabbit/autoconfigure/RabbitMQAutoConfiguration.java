//package com.ark.component.mq.rabbit.autoconfigure;
//
//import com.ark.component.mq.rabbit.DefaultConfirmCallback;
//import com.ark.component.mq.rabbit.DefaultReturnCallback;
//import com.ark.component.mq.rabbit.RabbitMQService;
//import com.ark.component.mq.rabbit.configuation.RabbitMQConfiguration;
//import com.ark.component.mq.rabbit.listener.RabbitMQListener;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.AmqpAdmin;
//import org.springframework.amqp.rabbit.connection.*;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
//import org.springframework.amqp.rabbit.core.RabbitOperations;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.boot.autoconfigure.amqp.*;
//import org.springframework.boot.autoconfigure.condition.*;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.context.properties.PropertyMapper;
//import org.springframework.context.annotation.*;
//
//import java.time.Duration;
//import java.util.Objects;
//
//
///**
// * MQ集成监听装配类
// */
//@Slf4j
//@EnableConfigurationProperties(RabbitMQConfiguration.class)
//public class RabbitMQAutoConfiguration {
//
//    public RabbitMQAutoConfiguration() {
//        log.info("enable [ark-component-mq-integration-spring-boot-starter]");
//    }
//
//    @Bean
//    @ConditionalOnProperty(
//            prefix = "ark.component.mq.rabbitmq",
//            value = "enabled",
//            havingValue = "true",
//            matchIfMissing = true)
//    @ConditionalOnMissingBean
//    public RabbitMQListener rabbitMqListener(RabbitMQConfiguration configuration) {
//        return new RabbitMQListener(configuration);
//    }
//
//    @Bean
////    @ConditionalOnProperty(
////            prefix = "ark.component.mq.rocketmq.producer",
////            value = "enabled",
////            havingValue = "true",
////            matchIfMissing = true)
//    @ConditionalOnMissingBean
//    public RabbitMQService rabbitMQService(RabbitMQConfiguration configuration, RabbitTemplate rabbitTemplate) {
//        return new RabbitMQService(configuration, rabbitTemplate);
//    }
//
//    @Primary
//    @Configuration(proxyBeanMethods = false)
//    @ConditionalOnMissingBean(ConnectionFactory.class)
//    protected static class RabbitConnectionFactoryCreator {
//
//    }
//
//    @Bean
//    @Primary
//    public CachingConnectionFactory rabbitConnectionFactory(RabbitMQConfiguration properties,
//                                                            ObjectProvider<ConnectionNameStrategy> connectionNameStrategy) throws Exception {
//        PropertyMapper map = PropertyMapper.get();
//        CachingConnectionFactory factory = new CachingConnectionFactory(
//                Objects.requireNonNull(getRabbitConnectionFactoryBean(properties).getObject()));
//        map.from(properties::determineAddresses).to(factory::setAddresses);
//        map.from(properties::isPublisherReturns).to(factory::setPublisherReturns);
//        map.from(properties::getPublisherConfirmType).whenNonNull().to(factory::setPublisherConfirmType);
//        RabbitMQConfiguration.Cache.Channel channel = properties.getCache().getChannel();
//        map.from(channel::getSize).whenNonNull().to(factory::setChannelCacheSize);
//        map.from(channel::getCheckoutTimeout).whenNonNull().as(Duration::toMillis)
//                .to(factory::setChannelCheckoutTimeout);
//        RabbitMQConfiguration.Cache.Connection connection = properties.getCache().getConnection();
//        map.from(connection::getMode).whenNonNull().to(factory::setCacheMode);
//        map.from(connection::getSize).whenNonNull().to(factory::setConnectionCacheSize);
//        map.from(connectionNameStrategy::getIfUnique).whenNonNull().to(factory::setConnectionNameStrategy);
//        return factory;
//    }
//
//    private RabbitConnectionFactoryBean getRabbitConnectionFactoryBean(RabbitMQConfiguration properties) {
//        PropertyMapper map = PropertyMapper.get();
//        RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
//        map.from(properties::determineUsername).whenNonNull().to(factory::setUsername);
//        map.from(properties::determinePassword).whenNonNull().to(factory::setPassword);
//        map.from(properties::determineVirtualHost).whenNonNull().to(factory::setVirtualHost);
//        map.from(properties::getRequestedHeartbeat).whenNonNull().asInt(Duration::getSeconds)
//                .to(factory::setRequestedHeartbeat);
//        map.from(properties::getRequestedChannelMax).to(factory::setRequestedChannelMax);
//        RabbitMQConfiguration.Ssl ssl = properties.getSsl();
//        if (ssl.determineEnabled()) {
//            factory.setUseSSL(true);
//            map.from(ssl::getAlgorithm).whenNonNull().to(factory::setSslAlgorithm);
//            map.from(ssl::getKeyStoreType).to(factory::setKeyStoreType);
//            map.from(ssl::getKeyStore).to(factory::setKeyStore);
//            map.from(ssl::getKeyStorePassword).to(factory::setKeyStorePassphrase);
//            map.from(ssl::getTrustStoreType).to(factory::setTrustStoreType);
//            map.from(ssl::getTrustStore).to(factory::setTrustStore);
//            map.from(ssl::getTrustStorePassword).to(factory::setTrustStorePassphrase);
//            map.from(ssl::isValidateServerCertificate)
//                    .to((validate) -> factory.setSkipServerCertificateValidation(!validate));
//            map.from(ssl::getVerifyHostname).to(factory::setEnableHostnameVerification);
//        }
//        map.from(properties::getConnectionTimeout).whenNonNull().asInt(Duration::toMillis)
//                .to(factory::setConnectionTimeout);
//        factory.afterPropertiesSet();
//        return factory;
//    }
//
//    @Primary
//    @Bean
//    public RabbitTemplate rabbitTemplate(
//            RabbitTemplate.ReturnCallback returnCallback,
//            RabbitTemplate.ConfirmCallback confirmCallback,
//            ConnectionFactory connectionFactory) {
//        RabbitTemplate template = new RabbitTemplate();
//        template.setConnectionFactory(connectionFactory);
//        template.setMandatory(true);
//        template.setConfirmCallback(confirmCallback);
//        template.setReturnCallback(returnCallback);
//        return template;
//    }
//
//    @Primary
//    @Bean
//    @ConditionalOnProperty(prefix = "ark.component.mq.rabbitmq", name = "dynamic", matchIfMissing = true)
//    @ConditionalOnMissingBean
//    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
//        return new RabbitAdmin(connectionFactory);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public RabbitTemplate.ConfirmCallback confirmCallback() {
//        return new DefaultConfirmCallback();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public RabbitTemplate.ReturnCallback returnCallback() {
//        return new DefaultReturnCallback();
//    }
//
//}
