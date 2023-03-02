package com.ark.component.mq.core.serializer;

import com.alibaba.fastjson.JSON;
import com.ark.component.mq.exception.MQCodecException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * FastJSON编解码器
 */
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
            log.error("MQ Message Encode Error", e);
            throw new MQCodecException(e);
        }
    }

    @Override
    public <T> T decode(byte[] bytes, Type typeArgument) {
        try {
            return JSON.parseObject(bytes, typeArgument);
        } catch (Exception e) {
            log.error("MQ Message Decode Error", e);
            throw new MQCodecException(e);
        }
    }

}
