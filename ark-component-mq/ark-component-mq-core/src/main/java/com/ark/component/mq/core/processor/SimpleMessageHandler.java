package com.ark.component.mq.core.processor;

import lombok.extern.slf4j.Slf4j;

/**
 * 如果不需要关注原生的Message对象直接继承这个类即可
 * @param <T>
 */
@Slf4j
public abstract class SimpleMessageHandler<T> extends StandardMessageHandler<T, Object> {
}
