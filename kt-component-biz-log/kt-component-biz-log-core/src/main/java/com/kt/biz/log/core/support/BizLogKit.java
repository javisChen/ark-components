package com.kt.biz.log.core.support;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class BizLogKit {

    public static String getClientType() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            String header = (((ServletRequestAttributes) requestAttributes)).getRequest().getHeader("user-agent");
            UserAgent parse = UserAgentUtil.parse(header);
            return parse.isMobile() ? "APP" : "PC";
        }
        return "-";
    }

}
