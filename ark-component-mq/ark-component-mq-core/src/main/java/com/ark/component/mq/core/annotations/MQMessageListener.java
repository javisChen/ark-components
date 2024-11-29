package com.ark.component.mq.core.annotations;

import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.MQType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MQMessageListener {

    MQType mq() default MQType.ROCKET;

    String consumerGroup() default "";

    String topic();

    String tags() default "*";

    ConsumeMode consumeMode() default ConsumeMode.CLUSTERING;

    int maxReconsumeTimes() default -1;

    long consumeTimeout() default 15L;

    int replyTimeout() default 3000;

    /**
     * 设置每次消费的消息数，某些业务流程如果支持批量方式消费，则可以很大程度上提高消费吞吐量，例如订单扣款类应用，一次处理一个订单耗时 1 s，一次处理 10 个订单可能也只耗时 2 s，这样即可大幅度提高消费的吞吐量
     */
    int consumeMessageBatchMaxSize() default 1;
}
