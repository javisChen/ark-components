package com.kt.component.mq.integration.rocket;

import com.kt.component.mq.MQMessageService;
import com.kt.component.mq.Message;
import com.kt.component.mq.integration.ApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RocketMQProducerTest extends ApplicationTests {

    @Autowired
    private MQMessageService mqMessageService;

    @Test
    public void send() {
        for (int i = 0; i < 10; i++) {
            mqMessageService.send("test", new Message("test msg" + i));
        }
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
