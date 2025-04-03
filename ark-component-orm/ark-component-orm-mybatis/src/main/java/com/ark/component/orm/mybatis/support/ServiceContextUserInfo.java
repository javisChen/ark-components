package com.ark.component.orm.mybatis.support;

import com.ark.component.context.core.ServiceContext;
import com.ark.component.security.base.authentication.AuthPrincipal;

public class ServiceContextUserInfo implements UserInfo {

    @Override
    public Long getCurrentUserId() {
        AuthPrincipal authPrincipal = ServiceContext.getPrincipal();
        if (authPrincipal != null) {
            return authPrincipal.getPrincipalId();
        }
        return 0L;
    }
}
