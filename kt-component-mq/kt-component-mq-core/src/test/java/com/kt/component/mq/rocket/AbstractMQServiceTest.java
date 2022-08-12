package com.kt.component.mq.rocket;

import com.kt.component.mq.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AbstractMQServiceTest extends ApplicationTests {

    @Autowired
    private MessageService messageService;

    @Test
    public void send() {
        for (int i = 0; i < 10; i++) {
            messageService.send("test", "test msg" + i);
        }
    }

    @Test
    public void asyncSend() {
//        for (int i = 0; i < 10; i++) {
            messageService.asyncSend("test", "test msg");
//        }
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}