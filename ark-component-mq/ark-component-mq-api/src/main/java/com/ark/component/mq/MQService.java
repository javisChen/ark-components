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
    SendResult send(String topic, MsgBody msg);

    SendResult send(String topic, MsgBody msg, int timeout);

    SendResult send(String topic, String tag, MsgBody msg);

    SendResult send(String topic, String tag, MsgBody msg, int timeout);

    /*
        同步发送延迟消息
     */
    SendResult delaySend(String topic, MsgBody msg, int delay);

    SendResult delaySend(String topic, MsgBody msg, int delay, int timeout);

    SendResult delaySend(String topic, String tag, int delay, MsgBody msg);

    SendResult delaySend(String topic, String tag, int delay, int timeout, MsgBody msg);

    /*
        异步发送
     */
    void asyncSend(String topic, MsgBody msg);

    void asyncSend(String topic, MsgBody msg, int timeout);

    void asyncSend(String topic, String tag, MsgBody msg);

    void asyncSend(String topic, String tag, MsgBody msg, int timeout);

    void asyncSend(String topic, MsgBody msg, SendConfirm callback);

    void asyncSend(String topic, MsgBody msg, int timeout, SendConfirm callback);

    void asyncSend(String topic, String tag, MsgBody msg, SendConfirm callback);

    void asyncSend(String topic, String tag, MsgBody msg, int timeout, SendConfirm callback);

    /*
        异步发送延迟消息
     */
    void delayAsyncSend(String topic, MsgBody msg, int delay, SendConfirm callback);

    void delayAsyncSend(String topic, MsgBody msg, int delay, int timeout, SendConfirm callback);

    void delayAsyncSend(String topic, String tag, int delay, MsgBody msg, SendConfirm callback);

    void delayAsyncSend(String topic, String tag, int delay, int timeout, MsgBody msg, SendConfirm callback);

}
