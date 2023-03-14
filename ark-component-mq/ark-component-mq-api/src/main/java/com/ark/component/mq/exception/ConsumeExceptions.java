package com.ark.component.mq.exception;

/**
 * 消费过程统一使用该工具类抛出异常
 */
public class ConsumeExceptions {

    /**
     * 丢弃消息
     */
    public static MessageDiscardException discard(String s) {
        return new MessageDiscardException(s);
    }

    /**
     * 把消息重新放回队列
     */
    public static MessageRequeueException requeue(String s) {
        return new MessageRequeueException(s);
    }
}
