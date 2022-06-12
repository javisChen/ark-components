package com.kt.component.mq.rocket;

import com.kt.component.mq.MqService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

class RocketMqLearnTest extends ApplicationTests {

    @Autowired
    private MqService mqService;

    @Test
    public void send() {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        try {
            String topic = "order_pay";
            Message message = new Message(topic, ("hello" + LocalDateTime.now()).getBytes(StandardCharsets.UTF_8));
            SendResult sendResult = defaultMQProducer.send(message);
            System.out.println(sendResult);
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        } catch (RemotingException e) {
            throw new RuntimeException(e);
        } catch (MQBrokerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}