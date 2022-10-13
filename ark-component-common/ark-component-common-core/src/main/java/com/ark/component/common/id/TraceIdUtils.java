package com.ark.component.common.id;

import cn.hutool.core.util.IdUtil;

public class TraceIdUtils {

    public static String getId() {
        return IdUtil.fastUUID();
    }
}
