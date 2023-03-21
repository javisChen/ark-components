//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ark.component.mq;


/**
 * 定义MQ常用发送消息接口
 *
 * @author victor
 */
public interface MQService {

    MQType mqType();

    /*
        同步发送
     */
    SendConfirm send(String topic, MsgBody msg);

    SendConfirm send(String topic, MsgBody msg, int timeout);

    SendConfirm send(String topic, String tag, MsgBody msg);

    SendConfirm send(String topic, String tag, MsgBody msg, int timeout);

    /*
        同步发送延迟消息
     */
    SendConfirm delaySend(String topic, MsgBody msg, int delay);

    SendConfirm delaySend(String topic, MsgBody msg, int delay, int timeout);

    SendConfirm delaySend(String topic, String tag, int delay, MsgBody msg);

    SendConfirm delaySend(String topic, String tag, int delay, int timeout, MsgBody msg);

    /*
        异步发送
     */
    void asyncSend(String topic, MsgBody msg);

    void asyncSend(String topic, MsgBody msg, int timeout);

    void asyncSend(String topic, String tag, MsgBody msg);

    void asyncSend(String topic, String tag, MsgBody msg, int timeout);

    void asyncSend(String topic, MsgBody msg, MQSendCallback callback);

    void asyncSend(String topic, MsgBody msg, int timeout, MQSendCallback callback);

    void asyncSend(String topic, String tag, MsgBody msg, MQSendCallback callback);

    void asyncSend(String topic, String tag, MsgBody msg, int timeout, MQSendCallback callback);

    /*
        异步发送延迟消息
     */
    void delayAsyncSend(String topic, MsgBody msg, int delay, MQSendCallback callback);

    void delayAsyncSend(String topic, MsgBody msg, int delay, int timeout, MQSendCallback callback);

    void delayAsyncSend(String topic, String tag, int delay, MsgBody msg, MQSendCallback callback);

    void delayAsyncSend(String topic, String tag, int delay, int timeout, MsgBody msg, MQSendCallback callback);

}
