package com.kt.component.mq.core.annotations;

import com.kt.component.mq.core.support.ConsumeMode;
import com.kt.component.mq.core.support.MQType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MQMessageListener {

    String NAME_SERVER_PLACEHOLDER = "${rocketmq.name-server:}";
    String ACCESS_KEY_PLACEHOLDER = "${rocketmq.consumer.access-key:}";
    String SECRET_KEY_PLACEHOLDER = "${rocketmq.consumer.secret-key:}";
    String TRACE_TOPIC_PLACEHOLDER = "${rocketmq.consumer.customized-trace-topic:}";
    String ACCESS_CHANNEL_PLACEHOLDER = "${rocketmq.access-channel:}";

    MQType mq() default MQType.ROCKET;

    String consumerGroup();

    String topic();

    String tags() default "*";

//    ConsumeMode consumeMode() default ConsumeMode.CONCURRENTLY;

    ConsumeMode consumeMode() default ConsumeMode.CLUSTERING;

    int maxReconsumeTimes() default -1;

    long consumeTimeout() default 15L;

    int replyTimeout() default 3000;

//    String accessKey() default "${rocketmq.consumer.access-key:}";
//
//    String secretKey() default "${rocketmq.consumer.secret-key:}";
//
//    boolean enableMsgTrace() default false;
//
//    String customizedTraceTopic() default "${rocketmq.consumer.customized-trace-topic:}";
//
//    String nameServer() default "${rocketmq.name-server:}";
//
//    String accessChannel() default "${rocketmq.access-channel:}";
}
