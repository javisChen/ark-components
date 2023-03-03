package com.ark.component.mq.integration.rocket;

import com.ark.component.mq.integration.ApplicationTests;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

public class RocketMQProducerTest extends ApplicationTests {

    public static void main(String[] args) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setProducerGroup("pg_edfault");
        defaultMQProducer.setNamesrvAddr("localhost:9876");
        defaultMQProducer.start();

        for (int i = 0; i < 10; i++) {
            SendResult send = defaultMQProducer.send(new Message(MQTestConst.TOPIC,
                    MQTestConst.TAG_BROAD_CASTING,
                    ("TEST MSG：" + i).getBytes(StandardCharsets.UTF_8)));
            System.out.println(send);
        }
    }
}
