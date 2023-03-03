package com.ark.component.mq;

public interface MQSendCallback {

    void onSuccess(MQSendResponse MQSendResponse);

    void onException(Throwable throwable);

}
