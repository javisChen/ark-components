//package com.ark.component.mq.kafka;
//
//import com.alibaba.fastjson.JSONObject;
//import com.ark.component.mq.MQType;
//import com.ark.component.mq.MsgBody;
//import com.ark.component.mq.SendConfirm;
//import com.ark.component.mq.SendResult;
//import com.ark.component.mq.exception.MQException;
//import com.ark.component.mq.kafka.configuation.KafkaConfiguration;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.ListenableFutureCallback;
//
//@Slf4j
//public class KafkaService extends AbstractMQService<Message, org.apache.rocketmq.client.producer.SendResult> {
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    public KafkaService(KafkaConfiguration mqConfiguration) {
//        super(mqConfiguration);
//    }
//
//    @Override
//    protected org.apache.rocketmq.client.producer.SendResult executeSend(String bizKey, String topic, String tag, Message msgBody, long timeout, int delayLevel) {
//        try {
//            ListenableFuture<org.springframework.kafka.support.SendResult<String, Object>> send
//                    = kafkaTemplate.send(topic, bizKey, null);
//            send.addCallback(new ListenableFutureCallback<>() {
//                @Override
//                public void onFailure(Throwable ex) {
//
//                }
//
//                @Override
//                public void onSuccess(org.springframework.kafka.support.SendResult<String, Object> result) {
//
//                }
//            });
//        } catch (Exception e) {
//            throw new MQException(e);
//        }
//    }
//
//    @Override
//    protected void executeAsyncSend(String bizKey,
//                                    String topic,
//                                    String tag,
//                                    Message message,
//                                    long timeout,
//                                    int delayLevel,
//                                    SendConfirm callback) {
//        try {
//            kafkaTemplate.send(message, new SendCallback() {
//                @Override
//                public void onSuccess(org.apache.rocketmq.client.producer.SendResult sendResult) {
//                    if (callback != null) {
//                        SendResult response = SendResult.builder()
//                                .withMsgId(sendResult.getMsgId())
//                                .withBizKey(bizKey)
//                                .build();
//                        callback.onSuccess(response);
//                    }
//                }
//
//                @Override
//                public void onException(Throwable throwable) {
//                    if (callback != null) {
//                        SendResult response = SendResult.builder()
//                                .withBizKey(bizKey)
//                                .withNote(throwable.getMessage())
//                                .withThrowable(throwable)
//                                .build();
//                        callback.onException(response);
//                    }
//                }
//            }, timeout);
//        } catch (Exception e) {
//            log.error("[RocketMQ]:send message error", e);
//            throw new MQException(e);
//        }
//    }
//
//    @Override
//    protected SendResult toResponse(org.apache.rocketmq.client.producer.SendResult sendResult, String bizKey) {
//        return SendResult.builder()
//                .withMsgId(sendResult.getMsgId())
//                .withBizKey(bizKey)
//                .build();
//    }
//
//    @Override
//    protected Message buildMessage(String topic, String tag, int delayLevel, MsgBody msgBodyPayLoad) {
//        Message message = new Message(topic, tag, msgBodyPayLoad.getBizKey(), JSONObject.toJSONBytes(msgBodyPayLoad));
//        message.setDelayTimeLevel(delayLevel);
//        return message;
//    }
//
//    @Override
//    public MQType mqType() {
//        return MQType.ROCKET;
//    }
//}
