package com.ark.component.mq.integration.rabbit.raw;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ark.component.mq.integration.MQTestConst;
import com.ark.component.mq.rabbit.support.Utils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitRawProducerTest {

    private final static String exchange = MQTestConst.TOPIC_ORDER;
    private final static String routingKey = "";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // queue：队列名
            // durable：是否持久化
            // exclusive：是否排外  即只允许该channel访问该队列   一般等于true的话用于一个队列只能有一个消费者来消费的场景
            // autoDelete：是否自动删除  消费完删除
            // arguments：其他属性
            String queue = Utils.createQueueName(exchange, routingKey, BuiltinExchangeType.FANOUT);
            // 声明交换机
            channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT, true, false, false, null);
            // 声明队列
            channel.queueDeclare(queue, true, false, false, null);
            // 队列绑定
            channel.queueBind(queue, exchange, routingKey);
            // 同一时刻服务器只会发一条消息d给消费者
            JSONObject order = new JSONObject();
            order.put("orderId", IdUtil.fastSimpleUUID());
            for (int i = 0; i < 1; i++) {
                String message = order.toJSONString();
                AMQP.BasicProperties props = new AMQP.BasicProperties()
                        .builder()
                        .messageId(IdUtil.fastSimpleUUID())
                        .build();
                channel.basicPublish(exchange, "", props, message.getBytes());
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }
}
