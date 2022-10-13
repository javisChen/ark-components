package com.ark.component.mq.rocket;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class TransactionProducer {

    public static void main(String[] args) {
        String producer_group = "producer_group";
        TransactionMQProducer producer = new TransactionMQProducer(producer_group);
        producer.setNamesrvAddr("localhost:9876");
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                System.out.println("执行本地事务");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return LocalTransactionState.UNKNOW;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                System.out.println("检查本地事务");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });
        try {
            producer.start();
            String topic = "order_pay";
            for (int i = 0; i < 10; i++) {
                Message message = new Message(topic, ("hello-" + i + "-" + LocalDateTime.now()).getBytes(StandardCharsets.UTF_8));
                producer.sendMessageInTransaction(message, null);
            }
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
    }
}
