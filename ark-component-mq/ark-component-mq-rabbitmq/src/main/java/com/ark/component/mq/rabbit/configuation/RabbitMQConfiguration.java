package com.ark.component.mq.rabbit.configuation;

import com.ark.component.mq.configuation.MQConfiguration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

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

}
