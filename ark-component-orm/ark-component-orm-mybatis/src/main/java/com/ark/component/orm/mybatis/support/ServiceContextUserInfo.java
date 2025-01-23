package com.ark.component.orm.mybatis.support;

import com.ark.component.context.core.ServiceContext;
import com.ark.component.security.base.authentication.AuthUser;

public class ServiceContextUserInfo implements UserInfo {

    @Override
    public Long getCurrentUserId() {
        AuthUser authUser = ServiceContext.getCurrentUser();
        if (authUser != null) {
            return authUser.getUserId();
        }
        return 0L;
    }
}
