package com.kt.component.mq.core.listener;

import com.kt.component.mq.core.support.ConsumeMode;
import com.kt.component.mq.core.support.MQType;
import lombok.Data;

@Data
public class MQListenerConfig {

    /**
     * 消费超时时间
     */
    Long consumeTimeout = 15L;

    /**
     * 消费组
     */
    String consumerGroup;

    /**
     * topic
     */
    private String topic;

    /**
     * tag
     */
    private String tag;

    /**
     * 消费模式
     * @see ConsumeMode
     */
    private ConsumeMode consumeMode;

    /**
     * 所使用的的MQ
     * @see MQType
     */
    private MQType mqType;

}
