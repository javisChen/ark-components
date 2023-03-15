package com.ark.component.mq.rabbit.configuation;

import com.ark.component.mq.configuation.MQConfiguration;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.amqp.core.Exchange;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MQ配置
 * @author jc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "ark.component.mq.rabbitmq")
public class RabbitMQConfiguration extends MQConfiguration {

    /**
     * Login user to authenticate to the broker.
     */
    private String username = "guest";

    /**
     * Login to authenticate against the broker.
     */
    private String password = "guest";

    /**
     * Virtual host to use when connecting to the broker.
     */
    private String virtualHost;

    /**
     * 交换机配置，通过提前配置好使用的交换和类型。
     * 在使用Rabbit发送消息的时候就可以直接使用统一API，交换机类型根据交换机名称获取
     */
    private List<Exchange> exchanges = Collections.emptyList();

    @Data
    public static class Exchange {

        /**
         * 交换机名字
         */
        private String name;

        /**
         * 交换机类型
         */
        private BuiltinExchangeType type;
    }

}
