package com.kt.component.mq.rocket;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Producer {

    public static void main(String[] args) {
        String producer_group = "producer_group";
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(producer_group);
        defaultMQProducer.setNamesrvAddr("localhost:9876");
        try {
            String topic = "order_pay";
            defaultMQProducer.start();
            for (int i = 0; i < 1; i++) {
                Message message = new Message(topic, ("hello-" + i + "-" + LocalDateTime.now()).getBytes(StandardCharsets.UTF_8));
                defaultMQProducer.send(message, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        System.out.println(sendResult);
                    }

                    @Override
                    public void onException(Throwable e) {

                    }
                });
            }
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        } catch (RemotingException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
