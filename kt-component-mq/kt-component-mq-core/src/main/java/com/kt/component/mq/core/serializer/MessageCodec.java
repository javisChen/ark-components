package com.kt.component.mq.core.serializer;

import com.alibaba.fastjson.TypeReference;
import com.kt.component.mq.Message;

import java.lang.reflect.Type;

public interface MessageCodec {

    byte[] encode(Object data);

    <T> T decode(byte[] bytes, Type typeArgument);

}
