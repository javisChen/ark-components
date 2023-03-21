package com.ark.component.mq.kafka.processor;

import com.ark.component.mq.core.processor.StandardMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;

/**
 * 一个处理消息的框架。极大多数情况，只需要继承当前Processor
 * 1.消息反序列化
 * 2.消息幂等校验
 * 3.消息处理
 * @param <T>
 */
@Slf4j
public abstract class RocketMessageHandler<T> extends StandardMessageHandler<T, SendResult> {

}
