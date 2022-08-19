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

    MessageResponse send(String topic, MessagePayLoad msg);

    MessageResponse send(String topic, MessagePayLoad msg, int timeout);

    MessageResponse send(String topic, String tag, MessagePayLoad msg);

    MessageResponse send(String topic, String tag, MessagePayLoad msg, int timeout);

    MessageResponse delaySend(String topic, MessagePayLoad msg, int delay);

    MessageResponse delaySend(String topic, MessagePayLoad msg, int delay, int timeout);

    MessageResponse delaySend(String topic, String tag, int delay, MessagePayLoad msg);

    MessageResponse delaySend(String topic, String tag, int delay, int timeout, MessagePayLoad msg);

    void asyncSend(String topic, MessagePayLoad msg);

    void asyncSend(String topic, MessagePayLoad msg, int timeout);

    void asyncSend(String topic, String tag, MessagePayLoad msg);

    void asyncSend(String topic, String tag, MessagePayLoad msg, int timeout);

    void asyncSend(String topic, MessagePayLoad msg, MessageSendCallback callback);

    void asyncSend(String topic, MessagePayLoad msg, int timeout, MessageSendCallback callback);

    void asyncSend(String topic, String tag, MessagePayLoad msg, MessageSendCallback callback);

    void asyncSend(String topic, String tag, MessagePayLoad msg, int timeout, MessageSendCallback callback);

    void delayAsyncSend(String topic, MessagePayLoad msg, int delay, MessageSendCallback callback);

    void delayAsyncSend(String topic, MessagePayLoad msg, int delay, int timeout, MessageSendCallback callback);

    void delayAsyncSend(String topic, String tag, int delay, MessagePayLoad msg, MessageSendCallback callback);

    void delayAsyncSend(String topic, String tag, int delay, int timeout, MessagePayLoad msg, MessageSendCallback callback);

}
