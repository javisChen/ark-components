package com.kt.component.mq.core.generator;

import cn.hutool.core.util.IdUtil;

public class DefaultMessageIdGenerator implements MessageIdGenerator {

    @Override
    public String getId() {
        return IdUtil.fastUUID();
    }

}
