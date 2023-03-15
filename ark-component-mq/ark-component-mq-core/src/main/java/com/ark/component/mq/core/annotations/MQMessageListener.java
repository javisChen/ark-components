package com.ark.component.mq.core.annotations;

import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.MQType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MQMessageListener {

    MQType mq() default MQType.ROCKET;

    String consumerGroup();

    String topic();

    String tags() default "*";

//    ConsumeMode consumeMode() default ConsumeMode.CONCURRENTLY;

    ConsumeMode consumeMode() default ConsumeMode.CLUSTERING;

    int maxReconsumeTimes() default -1;

    long consumeTimeout() default 15L;

    int replyTimeout() default 3000;
}
