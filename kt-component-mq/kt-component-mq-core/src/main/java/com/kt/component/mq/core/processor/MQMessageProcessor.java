package com.kt.component.mq.core.processor;

public interface MQMessageProcessor {

    boolean process(String msgId, byte[] body);

}
