package com.ark.component.mq.core.generator;

import cn.hutool.core.lang.generator.SnowflakeGenerator;

public class DefaultMsgIdGenerator implements MsgIdGenerator {

    private final SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Override
    public String getId() {
        return snowflakeGenerator.next().toString();
    }

}
