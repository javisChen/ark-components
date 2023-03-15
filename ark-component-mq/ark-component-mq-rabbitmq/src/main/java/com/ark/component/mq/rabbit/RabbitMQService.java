package com.ark.component.mq.rabbit;

import com.alibaba.fastjson.JSON;
import com.ark.component.mq.MQSendCallback;
import com.ark.component.mq.MQSendResponse;
import com.ark.component.mq.MsgBody;
import com.ark.component.mq.core.AbstractMQService;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.rabbit.configuation.RabbitMQConfiguration;
import com.ark.component.mq.rabbit.support.ConnectionManager;
import com.ark.component.mq.rabbit.support.Utils;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class RabbitMQService extends AbstractMQService<byte[], MQSendResponse> {

    private final RabbitMQConfiguration mqConfiguration;

    private Channel channel;

    public RabbitMQService(RabbitMQConfiguration mqConfiguration) {
        super(mqConfiguration);
        this.mqConfiguration = mqConfiguration;
        init();
    }

    private synchronized void init() {
        if (this.channel != null  && this.channel.isOpen()) {
            log.info("[RabbitMQ]:已存在可用连接 {}", channel.getConnection());
            return;
        }

        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection(this.mqConfiguration);
            this.channel = connection.createChannel();

            // 一旦消息被投递到所有匹配的队列之后，broker就会发送一个确认给生产者(包含消息的唯一ID)，
            // 这就使得生产者知道消息已经正确到达目的队列了，如果消息和队列是可持久化的，
            // 那么确认消息会在将消息写入磁盘之后发出，broker回传给生产者的确认消息中delivery-tag域包含了确认消息的序列号，
            // 此外broker也可以设置basic.ack的multiple域，
            // 表示到这个序列号之前的所有消息都已经得到了处理
            this.channel.confirmSelect();

            // 添加确认监听器，消息成功发送后broker会回传确认消息，最终回调给监听器
            this.channel.addConfirmListener(confirmListener());

            // mandatory=true时, 如果消息不能路由到指定的队列时，
            // 则会调用basic.return方法将消息返回给生产者,会触发addReturnListener注册的监听器
            this.channel.addReturnListener(returnCallback());
        } catch (Exception e) {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    protected byte[] buildMessage(String topic, String tag, int delayLevel, MsgBody msgBody) {
        return JSON.toJSONBytes(msgBody);
    }

    @Override
    protected MQSendResponse executeSend(String bizKey, String exchange, String routingKey, byte[] msgBody, long timeout, int delayLevel) {
        try {
            BuiltinExchangeType exchangeType = getExchangeType(exchange);

            String queue = Utils.createQueueName(exchange, routingKey, exchangeType);
            // 声明交换机
            channel.exchangeDeclare(exchange, exchangeType, true, false, false, null);
            // 声明队列
            channel.queueDeclare(queue, true, false, false, null);
            // 队列绑定
            channel.queueBind(queue, exchange, routingKey);

            // 如果为true, 消息不能路由到指定的队列时会触发addReturnListener注册的监听器；如果为false，则broker会直接将消息丢弃
            boolean mandatory = true;

            // 如果为true,当exchange将消息路由到queue时发现queue上没有消费者，那么这条消息不会放入队列中，该消息会通过basic.return方法返还给生产者。
            boolean immediate = false;

            String messageId = getMsgIdGenerator().getId();
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                    .builder()
                    .messageId(messageId)
                    .build();

            channel.basicPublish(exchange, routingKey, mandatory, immediate, basicProperties, msgBody);

            return MQSendResponse.builder()
                    .withBizKey(bizKey)
                    .withMsgId(messageId)
                    .build();
        } catch (Exception e) {
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
    protected void executeAsyncSend(String bizKey, String topic, String tag, byte[] body, long timeout, int delayLevel, MQSendCallback callback) {
        executeSend(bizKey, topic, tag, body, timeout, delayLevel);
    }

    @Override
    protected MQSendResponse convertToMQResponse(MQSendResponse sendResult, String bizKey) {
        return sendResult;
    }

    private ReturnCallback returnCallback() {
        return aReturn -> log.info("[RabbitMQ]:路由到队列失败：{}", aReturn);
    }

    private ConfirmListener confirmListener() {
        return new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                log.info("deliveryTag = {} 发送成功, multiple = {}", deliveryTag, multiple);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                log.info("deliveryTag = {} 发送失败, multiple = {}", deliveryTag, multiple);
            }
        };
    }
}
