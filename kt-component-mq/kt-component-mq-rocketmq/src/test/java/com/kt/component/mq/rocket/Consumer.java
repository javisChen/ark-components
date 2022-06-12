package com.kt.component.mq.rocket;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {

    private static Logger log = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) {
        String orderGroup = "order_group";
        String topic = "order_pay";
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(orderGroup);
        consumer.setNamesrvAddr("localhost:9876");
        try {
            consumer.subscribe(topic, "*");
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                for (MessageExt messageExt : msgs) {
                    log.debug("received msg: {}", messageExt);
                    try {
                        long now = System.currentTimeMillis();
                        long costTime = System.currentTimeMillis() - now;
                        log.debug("consume {} cost: {} ms", messageExt.getMsgId(), costTime);
                    } catch (Exception e) {
                        log.warn("consume message failed. messageId:{}, topic:{}, reconsumeTimes:{}", messageExt.getMsgId(), messageExt.getTopic(), messageExt.getReconsumeTimes(), e);
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            });
            consumer.start();
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        System.out.println("消费者已启动");
        try {
            Thread.sleep(10000000000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
