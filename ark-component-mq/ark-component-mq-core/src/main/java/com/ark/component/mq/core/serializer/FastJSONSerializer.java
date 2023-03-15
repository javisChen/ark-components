package com.ark.component.mq.core.serializer;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.ark.component.mq.exception.MQSerializerException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * FastJSON编解码器
 */
@Slf4j
public class FastJSONSerializer implements MessageSerializer {

    @Override
    public byte[] serialize(Object data) {
        if (Objects.isNull(data)) {
            return null;
        }
        try {
            return JSON.toJSONBytes(data);
        } catch (Exception e) {
            log.error("[MQ]：序列化失败", e);
            throw new MQSerializerException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Type typeArgument) {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        try {
            return JSON.parseObject(bytes, typeArgument);
        } catch (Exception e) {
            log.error("[MQ]：反序列化失败", e);
            throw new MQSerializerException(e);
        }
    }

}
