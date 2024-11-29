package com.ark.component.mq.core.listener;

import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.MQType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
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

    /**
     * 某些业务流程如果支持批量方式消费，则可以很大程度上提高消费吞吐量，例如订单扣款类应用，一次处理一个订单耗时 1 s，一次处理 10 个订单可能也只耗时 2 s，这样即可大幅度提高消费的吞吐量
     */
    int consumeMessageBatchMaxSize;

}
