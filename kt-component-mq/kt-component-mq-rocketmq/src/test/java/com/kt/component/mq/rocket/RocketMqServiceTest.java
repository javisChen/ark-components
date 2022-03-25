package com.kt.component.mq.rocket;

import com.kt.component.mq.MqService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RocketMqServiceTest extends ApplicationTests {

    @Autowired
    private MqService mqService;

    @Test
    public void send() {
        for (int i = 0; i < 10; i++) {
            mqService.send("test", "test msg" + i);
        }
    }

    @Test
    public void asyncSend() {
//        for (int i = 0; i < 10; i++) {
            mqService.asyncSend("test", "test msg");
//        }
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}