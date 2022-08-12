package com.kt.component.mq.rocket;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

public class Producer {

    private static Logger log = LoggerFactory.getLogger(Producer.class);

    public static void main(String[] args) {
        String producer_group = "producer_group";
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(producer_group);
        defaultMQProducer.setNamesrvAddr("localhost:9876");
        try {
            String topic = "order_pay";
            defaultMQProducer.start();
            for (int i = 0; i < 1; i++) {
                Message message = new Message(topic, ("hello-" + i + "-" + LocalDateTime.now()).getBytes(StandardCharsets.UTF_8));
                message.setKeys(UUID.randomUUID().toString());
                SendCallback sendCallback = new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        String keys = message.getKeys();
                        log.info("msgId: {}, key: {}", sendResult.getMsgId(), keys);
                    }

                    @Override
                    public void onException(Throwable e) {

                    }
                };
//                defaultMQProducer.send(message, sendCallback);
                SendResult send = defaultMQProducer.send(message);
                log.info("result : {} ", send);
            }
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        } catch (RemotingException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (MQBrokerException e) {
            throw new RuntimeException(e);
        }
    }
}
