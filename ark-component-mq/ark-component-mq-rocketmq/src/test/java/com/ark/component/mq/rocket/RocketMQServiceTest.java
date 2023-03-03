package com.ark.component.mq.rocket;

import com.ark.component.mq.MsgBody;
import com.ark.component.mq.MQService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RocketMQServiceTest extends ApplicationTests {

    @Autowired
    private MQService mqService;

    @Test
    public void send() {
        for (int i = 0; i < 10; i++) {
            mqService.send("test", new MsgBody("test msg" + i));
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
        mqService.asyncSend("test", new MsgBody("test msg"));
//        }
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}