//package com.kt.component.mq.rabbit;
//
//import com.alibaba.fastjson.JSON;
//import com.kt.component.mq.MessagePayLoad;
//import com.kt.component.mq.MessageResponse;
//import com.kt.component.mq.MessageSendCallback;
//import com.kt.component.mq.MessageService;
//import com.kt.component.mq.configuation.MQConfiguration;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.rocketmq.client.producer.SendCallback;
//import org.apache.rocketmq.client.producer.SendResult;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageBuilder;
//
//@Slf4j
//public class RabbitMessageService implements MessageService {
//
//    private final MQConfiguration mqConfiguration;
//
//    private final AmqpTemplate amqpTemplate;
//
//    public RabbitMessageService(AmqpTemplate amqpTemplate) {
//        this.amqpTemplate = amqpTemplate;
//        this.mqConfiguration = new MQConfiguration();
//    }
//
//    public RabbitMessageService(MQConfiguration mqConfiguration, AmqpTemplate amqpTemplate) {
//        this.mqConfiguration = mqConfiguration;
//        this.amqpTemplate = amqpTemplate;
//    }
//
//    @Override
//    public MessageResponse send(String topic, MessagePayLoad msg) {
//        Message build = MessageBuilder.withBody((byte[]) msg).build();
//        Message message = amqpTemplate.sendAndReceive(topic, build);
//        MessageResponse.builder().withMsgId()
//        return message;
//    }
//
//    @Override
//    public MessageResponse send(String topic, MessagePayLoad msg, int timeout) {
//        return null;
//    }
//
//    @Override
//    public MessageResponse send(String topic, String tag, MessagePayLoad msg) {
//        return null;
//    }
//
//    @Override
//    public MessageResponse send(String topic, String tag, MessagePayLoad msg, int timeout) {
//        return null;
//    }
//
//    @Override
//    public MessageResponse delaySend(String topic, MessagePayLoad msg, int delay) {
//        return null;
//    }
//
//    @Override
//    public MessageResponse delaySend(String topic, MessagePayLoad msg, int delay, int timeout) {
//        return null;
//    }
//
//    @Override
//    public MessageResponse delaySend(String topic, String tag, int delay, MessagePayLoad msg) {
//        return null;
//    }
//
//    @Override
//    public MessageResponse delaySend(String topic, String tag, int delay, int timeout, MessagePayLoad msg) {
//        return null;
//    }
//
//    @Override
//    public void asyncSend(String topic, MessagePayLoad msg) {
//
//    }
//
//    @Override
//    public void asyncSend(String topic, MessagePayLoad msg, int timeout) {
//
//    }
//
//    @Override
//    public void asyncSend(String topic, String tag, MessagePayLoad msg) {
//
//    }
//
//    @Override
//    public void asyncSend(String topic, String tag, MessagePayLoad msg, int timeout) {
//
//    }
//
//    @Override
//    public void asyncSend(String topic, MessagePayLoad msg, MessageSendCallback callback) {
//
//    }
//
//    @Override
//    public void asyncSend(String topic, MessagePayLoad msg, int timeout, MessageSendCallback callback) {
//
//    }
//
//    @Override
//    public void asyncSend(String topic, String tag, MessagePayLoad msg, MessageSendCallback callback) {
//
//    }
//
//    @Override
//    public void asyncSend(String topic, String tag, MessagePayLoad msg, int timeout, MessageSendCallback callback) {
//
//    }
//
//    @Override
//    public void delayAsyncSend(String topic, MessagePayLoad msg, int delay, MessageSendCallback callback) {
//
//    }
//
//    @Override
//    public void delayAsyncSend(String topic, MessagePayLoad msg, int delay, int timeout, MessageSendCallback callback) {
//
//    }
//
//    @Override
//    public void delayAsyncSend(String topic, String tag, int delay, MessagePayLoad msg, MessageSendCallback callback) {
//
//    }
//
//    @Override
//    public void delayAsyncSend(String topic, String tag, int delay, int timeout, MessagePayLoad msg, MessageSendCallback callback) {
//
//    }
//}
