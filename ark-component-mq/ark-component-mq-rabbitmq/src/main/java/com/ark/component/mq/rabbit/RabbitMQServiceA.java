//package com.ark.component.mq.rabbit;
//
//import com.alibaba.fastjson.JSON;
//import com.ark.component.mq.MQSendCallback;
//import com.ark.component.mq.MQSendResponse;
//import com.ark.component.mq.MsgBody;
//import com.ark.component.mq.core.AbstractMQService;
//import com.ark.component.mq.exception.MQException;
//import com.ark.component.mq.rabbit.configuation.RabbitMQConfiguration;
//import com.rabbitmq.client.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.connection.RabbitUtils;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//
//@Slf4j
//public class RabbitMQServiceA extends AbstractMQService<byte[], MQSendResponse> {
//
//    private CachingConnectionFactory connectionFactory;
//
//    private org.springframework.amqp.rabbit.connection.Connection connection = null;
//
//    private RabbitTemplate rabbitTemplate;
//
//
//    private final RabbitMQConfiguration mqConfiguration;
//
//    private Channel channel;
//
//    public RabbitMQServiceA(RabbitMQConfiguration mqConfiguration) {
//        super(mqConfiguration);
//        this.mqConfiguration = mqConfiguration;
//        init();
//    }
//
//    private synchronized void init() {
//        if (this.rabbitTemplate != null) {
//            log.info("[RabbitMQ]:已存在可用连接 {}", channel.getConnection());
//            return;
//        }
//
//        try {
//            ConnectionFactory rabbitConnectionFactory = new ConnectionFactory();
//            rabbitConnectionFactory.setConnectionTimeout(mqConfiguration.getSendMessageTimeout());
//            rabbitConnectionFactory.setUsername(mqConfiguration.getUsername());
//            rabbitConnectionFactory.setPassword(mqConfiguration.getPassword());
//            rabbitConnectionFactory.setVirtualHost(mqConfiguration.getVirtualHost());
//            connectionFactory = new CachingConnectionFactory(rabbitConnectionFactory);
//            connectionFactory.setAddresses(mqConfiguration.getServer());
//            rabbitTemplate = new RabbitTemplate(connectionFactory);
//            rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
//                if (ack) {
//                    lo
//                }
//            });
//            rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey) -> {
//
//            });
//            log.info("[RabbitMQ]:连接服务器成功 {}", connectionFactory.getDelegate().getAddress().toString());
//        } catch (Exception e) {
//            RabbitUtils.closeConnection(connection);
//        }
//    }
//
//    @Override
//    protected byte[] buildMessage(String topic, String tag, int delayLevel, MsgBody msgBody) {
//        return JSON.toJSONBytes(msgBody);
//    }
//
//    @Override
//    protected MQSendResponse executeSend(String bizKey, String topic, String tag, byte[] msgBody, long timeout, int delayLevel) {
//        try {
//            String messageId = getMsgIdGenerator().getId();
//            MessageProperties messageProperties = new MessageProperties();
//            messageProperties.setMessageId(messageId);
//            rabbitTemplate.send(topic, tag, new Message(msgBody, messageProperties), new CorrelationData(messageId));
//
//            return MQSendResponse.builder()
//                    .withBizKey(bizKey)
//                    .withMsgId(messageId)
//                    .build();
//        } catch (Exception e) {
//            throw new MQException(e);
//        }
//    }
//
//
//    @Override
//    protected void executeAsyncSend(String topic, String tag, byte[] body, long timeout, int delayLevel, MQSendCallback callback, String bizKey) {
//
//    }
//
//    @Override
//    protected MQSendResponse convertToMQResponse(MQSendResponse sendResult, String bizKey) {
//        return sendResult;
//    }
//}
