//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kt.component.mq;


/**
 * 定义OSS通用接口
 * @author victor
 */
public interface MqService {


    void send(String topic, Object msg);

    void asyncSend(String topic, Object msg);
}
