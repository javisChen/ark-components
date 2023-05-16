package com.ark.component.mq.rabbit;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.ark.component.mq.SendConfirm;
import com.ark.component.mq.SendResult;
import com.ark.component.mq.MQType;
import com.ark.component.mq.MsgBody;
import com.ark.component.mq.core.AbstractMQService;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.rabbit.configuation.RabbitMQConfiguration;
import com.ark.component.mq.rabbit.support.ConfirmManager;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Slf4j
public class RabbitMQService extends AbstractMQService<Message, SendResult> {

    private final RabbitMQConfiguration mqConfiguration;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQService(RabbitMQConfiguration mqConfiguration, RabbitTemplate rabbitTemplate) {
        super(mqConfiguration);
        this.mqConfiguration = mqConfiguration;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    protected Message buildMessage(String topic, String tag, int delayLevel, MsgBody msgBody) {
        byte[] body = JSON.toJSONBytes(msgBody);
        MessageProperties properties = new MessageProperties();
        properties.setMessageId(IdUtil.fastUUID());
        properties.setTimestamp(new Date());
        properties.setContentEncoding(StandardCharsets.UTF_8.name());
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setContentLength(body.length);
        return new Message(body, properties);
    }

    @Override
    protected SendResult executeSend(String bizKey, String exchange, String routingKey, Message msgBody, long timeout, int delay) {
        try {
            if (delay > 0) {
                msgBody.getMessageProperties().setDelay(delay);
            }
            CorrelationData correlationData = buildCorrelationData(bizKey, msgBody);
            rabbitTemplate.sendAndReceive(exchange, routingKey, msgBody, correlationData);

            return SendResult.builder()
                    .withBizKey(bizKey)
                    .withMsgId(msgBody.getMessageProperties().getMessageId())
                    .build();
        } catch (Exception e) {
            throw new MQException(e);
        }
    }

    private CorrelationData buildCorrelationData(String bizKey, Message msgBody) {
        CorrelationData correlationData = new CorrelationData(bizKey);
        return correlationData;
    }

    @Override
    protected void executeAsyncSend(String bizKey,
                                    String exchange,
                                    String routingKey,
                                    Message msgBody,
                                    long timeout,
                                    int delay,
                                    SendConfirm callback) {
        String messageId = msgBody.getMessageProperties().getMessageId();
        try {
            if (delay > 0) {
                msgBody.getMessageProperties().setDelay(delay);
            }
            CorrelationData correlationData = buildCorrelationData(bizKey, msgBody);
            // 以messageId和bizKey把发送成功回调钩子存起来
            ConfirmManager.put(messageId, bizKey, callback);
            rabbitTemplate.send(exchange, routingKey, msgBody, correlationData);
        } catch (Exception e) {
            ConfirmManager.remove(messageId, bizKey);
            throw new MQException(e);
        }
    }

    /**
     * 根据交换机名称从配置中拿到具体的类型
     */
    private BuiltinExchangeType getExchangeType(String exchange) {
        List<RabbitMQConfiguration.Exchange> exchanges = mqConfiguration.getExchanges();
        return exchanges.stream()
                .filter(exc -> exc.getName().equals(exchange))
                .findFirst()
                .orElseThrow(() -> new MQException("[RabbitMQ]:Exchange[" + exchange + "]未配置"))
                .getType();
    }

    @Override
    protected SendResult toResponse(SendResult sendResult, String bizKey) {
        return sendResult;
    }

    @Override
    public MQType mqType() {
        return MQType.RABBIT;
    }
}
