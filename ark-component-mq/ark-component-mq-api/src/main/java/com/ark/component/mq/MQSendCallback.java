package com.ark.component.mq;

public interface MQSendCallback {

    void onSuccess(SendConfirm sendConfirm);

    void onException(SendConfirm sendConfirm);

}
