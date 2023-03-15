package com.ark.component.mq.core.serializer;

import java.lang.reflect.Type;

public interface MessageSerializer {

    byte[] serialize(Object data);

    <T> T deserialize(byte[] bytes, Type typeArgument);

}
