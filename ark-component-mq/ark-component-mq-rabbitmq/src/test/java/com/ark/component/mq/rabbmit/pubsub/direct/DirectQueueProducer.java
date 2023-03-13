package com.ark.component.mq.rabbmit.pubsub.direct;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

@Slf4j
public class DirectQueueProducer {

    private final static String EXCHANGE_NAME = "pub_sub_direct";

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
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            channel.confirmSelect();

            channel.addReturnListener(returnCallback());

            // 同一时刻服务器只会发一条消息d给消费者
            for (int i = 0; i < 10; i++) {
                String message = "订单：" + i;
                String messageId = new SnowflakeGenerator().next().toString();
                AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                        .builder()
                        .messageId(messageId)
                        .build();
                channel.basicPublish(EXCHANGE_NAME, "create_order", basicProperties, message.getBytes());
                System.out.println(" [x] Sent '" + message + "'");
            }
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static ReturnCallback returnCallback() {
        return aReturn -> log.info("[RabbitMQ]:路由到队列失败：{}", aReturn);
    }

    private static ConfirmListener confirmListener(String messageId, Consumer<String> consumer) {
        return new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
//                log.info("msgId = {} 发送成功, deliveryTag = {} , multiple = {}", messageId, deliveryTag, multiple);
                consumer.accept(messageId);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                log.info("deliveryTag = {} 发送失败, multiple = {}", deliveryTag, multiple);
            }
        };
    }

}
