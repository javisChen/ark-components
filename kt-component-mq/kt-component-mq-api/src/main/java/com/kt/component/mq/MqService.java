//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kt.component.mq;


/**
 * 定义OSS通用接口
 *
 * @author victor
 */
public interface MqService {
    void send(String topic, Object msg);

    void send(String topic, String tags, Object msg);

    void asyncSend(String topic, Object msg);

    void asyncSend(String topic, String tags, Object msg);

    void delaySend(String topic, String tags, Object msg, long delay);

    void asyncDelaySend(String topic, String tags, Object msg, long delay);

    void delaySend(String topic, Object msg, long delay);

    void asyncDelaySend(String topic, Object msg, long delay);
}
