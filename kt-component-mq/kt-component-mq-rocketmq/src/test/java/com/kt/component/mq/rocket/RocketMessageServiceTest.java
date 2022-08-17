package com.kt.component.mq.rocket;

import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RocketMessageServiceTest extends ApplicationTests {

    @Autowired
    private MessageService messageService;

    @Test
    public void send() {
        for (int i = 0; i < 10; i++) {
            messageService.send("test", new MessagePayLoad("test msg" + i));
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
        messageService.asyncSend("test", new MessagePayLoad("test msg"));
//        }
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}