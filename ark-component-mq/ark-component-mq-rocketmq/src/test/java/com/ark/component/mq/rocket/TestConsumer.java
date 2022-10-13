//package com.ark.component.mq.rocket;
//
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.apache.rocketmq.spring.annotation.MessageModel;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.List;
//
//@Component
//@RocketMQMessageListener(
//        messageModel = MessageModel.BROADCASTING,
//        consumerGroup = "default_group123",
//        topic = "test")
//public class TestConsumer implements RocketMQListener<String> {
//
//    @Override
//    public void onMessage(String message) {
//        System.out.println(new Date() + " message ->" + message);
//
//        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
//        defaultMQPushConsumer.setMessageListener(new MessageListenerConcurrently() {
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
//                for (MessageExt messageExt : list) {
//                }
//                return null;
//            }
//        });
//    }
//}
