package com.ark.component.mq.rabbit;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ark.component.mq.MQSendCallback;
import com.ark.component.mq.MQSendResponse;
import com.ark.component.mq.MsgBody;
import com.ark.component.mq.core.AbstractMQService;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.rabbit.configuation.RabbitMQConfiguration;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
public class RabbitMQService extends AbstractMQService<byte[], Object> {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

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
            connection = getConnection(this.mqConfiguration);
            log.info("[RabbitMQ]:连接服务器成功 {}", connection.getAddress().toString());
            this.channel = connection.createChannel();
        } catch (Exception e) {
            try {
                connection.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection(RabbitMQConfiguration rabbitMQConfiguration) throws IOException, TimeoutException {
        List<Address> addresses = resolveAddresses(rabbitMQConfiguration);
        return connectionFactory.newConnection(addresses);
    }

    /**
     * 解析服务端集群地址
     */
    private List<Address> resolveAddresses(RabbitMQConfiguration rabbitMQConfiguration) {
        return StrUtil.split(rabbitMQConfiguration.getServer(), ",", true, true).stream()
                .map(Address::parseAddress)
                .collect(Collectors.toList());
    }

    @Override
    protected byte[] buildMessage(String topic, String tag, int delayLevel, MsgBody msgBody) {
        return JSON.toJSONBytes(msgBody);
    }

    @Override
    protected Object executeSend(String topic, String tag, byte[] msgBody, long timeout, int delayLevel) {
        Connection connection;
        try {
            connection = getConnection(mqConfiguration);
        } catch (Exception e) {
            log.error("获取MQ连接失败", e);
            throw new MQException(e);
        }
        try {

            // 声明交换机
            channel.exchangeDeclare(topic, BuiltinExchangeType.DIRECT);

            // 如果为true, 消息不能路由到指定的队列时会触发addReturnListener注册的监听器；如果为false，则broker会直接将消息丢弃
            boolean mandatory = true;

            // 如果为true,当exchange将消息路由到queue时发现queue上没有消费者，那么这条消息不会放入队列中，该消息会通过basic.return方法返还给生产者。
            boolean immediate = false;
            //
            channel.basicPublish(topic, tag, mandatory, immediate, null, msgBody);
            // 一旦消息被投递到所有匹配的队列之后，broker就会发送一个确认给生产者(包含消息的唯一ID)，
            // 这就使得生产者知道消息已经正确到达目的队列了，如果消息和队列是可持久化的，
            // 那么确认消息会在将消息写入磁盘之后发出，broker回传给生产者的确认消息中delivery-tag域包含了确认消息的序列号，
            // 此外broker也可以设置basic.ack的multiple域，
            // 表示到这个序列号之前的所有消息都已经得到了处理
            channel.confirmSelect();

            channel.addConfirmListener(confirmListener());

            channel.addReturnListener(returnCallback());
        } catch (Exception e) {
            log.error("[RabbitMQ]");
            throw new MQException(e);
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                }
            } catch (Exception e) {
                log.error("[RabbitMQ]：关闭channel失败", e);
            }
            try {
                connection.close();
            } catch (Exception e) {
                log.error("[RabbitMQ]：关闭connection失败", e);
            }
        }
        return null;
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

    @Override
    protected void executeAsyncSend(String topic, String tag, byte[] body, long timeout, int delayLevel, MQSendCallback callback, String bizKey) {

    }

    @Override
    protected MQSendResponse convertToMQResponse(Object sendResult, String bizKey) {
        return null;
    }
}
