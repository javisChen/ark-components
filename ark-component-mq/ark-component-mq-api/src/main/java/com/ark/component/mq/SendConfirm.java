package com.ark.component.mq;

public interface SendConfirm {

    void onSuccess(SendResult sendResult);

    void onException(SendResult sendResult);

}
