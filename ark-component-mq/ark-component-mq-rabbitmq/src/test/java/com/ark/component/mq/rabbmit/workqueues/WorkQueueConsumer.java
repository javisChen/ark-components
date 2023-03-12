package com.ark.component.mq.rabbmit.workqueues;

import com.ark.component.mq.rabbmit.MyDefaultConsumer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkQueueConsumer {

    private final static String QUEUE_NAME = "work_queues";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection()) {
            try (Channel channel = connection.createChannel()) {
                // queue：队列名
                // durable：是否持久化
                // exclusive：是否排外  即只允许该channel访问该队列   一般等于true的话用于一个队列只能有一个消费者来消费的场景
                // autoDelete：是否自动删除  消费完删除
                // arguments：其他属性
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);

                // 定义队列的消费者

                // 监听队列
                channel.basicConsume(QUEUE_NAME, false, new MyDefaultConsumer(channel));

                Thread.sleep(100000000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}