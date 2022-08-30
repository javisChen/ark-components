package com.kt.component.mq.rocket;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class RocketMQListenTestType {

//    @MQMessageListener(consumerGroup = "default_group", topic = "test")
//    class RocketMQMessageProcessor implements MQMessageProcessor {
//        @Override
//        public boolean process(String msgId, byte[] body, Object raw) {
//            log.info("RocketMQMessageProcessor consume：" + msgId);
//            return true;
//        }
//    }

    @Test
    public void test() {
//        MQConfiguration mqConfiguration = new Config().mqConfiguration();
//        new MQListenerRunner(mqConfiguration).doListen(new RocketMQMessageProcessor());

//        try {
//            Thread.sleep(1000000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }
}
