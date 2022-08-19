package com.kt.component.mq.integration.configuration;

import com.kt.component.mq.rocket.configuation.RocketMQConfiguration;
import lombok.Data;

/**
 * MQ配置
 *
 * @author jc
 */
@Data
public class MQIntegrationConfiguration {

    private RocketMQConfiguration rocketMQ;

}
