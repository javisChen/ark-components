//package com.ark.component.mq.rabbit;
//
//import com.ark.component.mq.SendConfirm;
//import com.ark.component.mq.SendResult;
//import com.ark.component.mq.exception.MQException;
//import com.ark.component.mq.rabbit.support.ConfirmManager;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//
//@Slf4j
//public class DefaultConfirmCallback implements RabbitTemplate.ConfirmCallback {
//
//    @Override
//    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        log.info("[RabbitMQ]:接收消息发送结果,ack={}, correlationData={}, cause={}", ack, correlationData, cause);
//        String bizKey = null;
//        String messageId = null;
//        if (correlationData != null && correlationData.getReturnedMessage() != null) {
//            bizKey = correlationData.getId();
//            MessageProperties messageProperties = correlationData.getReturnedMessage().getMessageProperties();
//            messageId = messageProperties.getMessageId();
//        }
//        log.info("[RabbitMQ]:消息发送结果确认,msgId={}, bizKey={}", messageId, bizKey);
//        SendConfirm callback = ConfirmManager.get(messageId, bizKey);
//        try {
//            SendResult.SendConfirmBuilder confirmBuilder = SendResult.builder()
//                    .withBizKey(bizKey)
//                    .withMsgId(messageId);
//            if (ack) {
//                if (callback != null) {
//                    callback.onSuccess(confirmBuilder.build());
//                }
//            } else {
//                if (callback != null) {
//                    SendResult response = confirmBuilder
//                            .withThrowable(new MQException(cause))
//                            .withNote(cause)
//                            .build();
//                    callback.onException(response);
//                }
//            }
//            log.info("[RabbitMQ]:消息发送结果确认处理完成,msgId={}, bizKey={}", messageId, bizKey);
//        } catch (Exception e) {
//            log.info("[RabbitMQ]:消息发送结果确认处理出错,msgId={}, bizKey={}", messageId, bizKey);
//            throw e;
//        } finally {
//            ConfirmManager.remove(messageId, bizKey);
//        }
//    }
//
//}
