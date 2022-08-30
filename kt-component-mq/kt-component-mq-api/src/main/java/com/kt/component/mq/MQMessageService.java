//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kt.component.mq;


/**
 * 定义MQ常用发送消息接口
 *
 * @author victor
 */
public interface MQMessageService {

    MessageResponse send(String topic, Message msg);

    MessageResponse send(String topic, Message msg, int timeout);

    MessageResponse send(String topic, String tag, Message msg);

    MessageResponse send(String topic, String tag, Message msg, int timeout);

    MessageResponse delaySend(String topic, Message msg, int delay);

    MessageResponse delaySend(String topic, Message msg, int delay, int timeout);

    MessageResponse delaySend(String topic, String tag, int delay, Message msg);

    MessageResponse delaySend(String topic, String tag, int delay, int timeout, Message msg);

    void asyncSend(String topic, Message msg);

    void asyncSend(String topic, Message msg, int timeout);

    void asyncSend(String topic, String tag, Message msg);

    void asyncSend(String topic, String tag, Message msg, int timeout);

    void asyncSend(String topic, Message msg, MessageSendCallback callback);

    void asyncSend(String topic, Message msg, int timeout, MessageSendCallback callback);

    void asyncSend(String topic, String tag, Message msg, MessageSendCallback callback);

    void asyncSend(String topic, String tag, Message msg, int timeout, MessageSendCallback callback);

    void delayAsyncSend(String topic, Message msg, int delay, MessageSendCallback callback);

    void delayAsyncSend(String topic, Message msg, int delay, int timeout, MessageSendCallback callback);

    void delayAsyncSend(String topic, String tag, int delay, Message msg, MessageSendCallback callback);

    void delayAsyncSend(String topic, String tag, int delay, int timeout, Message msg, MessageSendCallback callback);

}
