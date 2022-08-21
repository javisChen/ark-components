package com.kt.component.mq.core.serializer;

import cn.hutool.core.util.TypeUtil;
import com.alibaba.fastjson.JSON;
import com.kt.component.mq.exception.MQException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Objects;

@Slf4j
public class FastJSONCodec implements MessageCodec {

    @Override
    public byte[] encode(Object data) {
        if (Objects.isNull(data)) {
            return null;
        }
        try {
            return JSON.toJSONBytes(data);
        } catch (Exception e) {
            log.error("mq message encode error", e);
            throw new MQException(e);
        }
    }

    @Override
    public <T> T decode(byte[] bytes, Type typeArgument) {
        try {
            return JSON.parseObject(bytes, typeArgument);
        } catch (Exception e) {
            log.error("mq message decode error", e);
            throw new MQException(e);
        }
    }

}
