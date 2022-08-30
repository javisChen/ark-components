package com.kt.component.mq.rocket.processor;

import com.kt.component.mq.core.processor.StandardMQMessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 一个处理消息的框架。极大多数情况，只需要继承当前Processor
 * 1.消息反序列化
 * 2.消息幂等校验
 * 3.消息处理
 * @param <T>
 */
@Slf4j
public abstract class RocketMQMessageProcessor<T> extends StandardMQMessageProcessor<T, MessageExt> {

}
