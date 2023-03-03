package com.ark.component.mq.core.generator;

import cn.hutool.core.util.IdUtil;

public class DefaultMsgIdGenerator implements MsgIdGenerator {

    @Override
    public String getId() {
        return IdUtil.fastUUID();
    }

}
