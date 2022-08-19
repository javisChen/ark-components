package com.kt.component.mq.rocket;

import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.MQMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RocketMQMessageServiceTest extends ApplicationTests {

    @Autowired
    private MQMessageService mqMessageService;

    @Test
    public void send() {
        for (int i = 0; i < 10; i++) {
            mqMessageService.send("test", new MessagePayLoad("test msg" + i));
        }
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void asyncSend() {
//        for (int i = 0; i < 10; i++) {
        mqMessageService.asyncSend("test", new MessagePayLoad("test msg"));
//        }
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}