package com.kt.component.mq;

public interface MessageSendCallback {

    void onSuccess(MessageResponse messageResponse);

    void onException(Throwable throwable);

}
