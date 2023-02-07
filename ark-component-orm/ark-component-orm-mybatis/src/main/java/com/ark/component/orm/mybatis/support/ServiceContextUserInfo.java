package com.ark.component.orm.mybatis.support;

import com.ark.component.context.core.ServiceContext;
import com.ark.component.security.base.user.LoginUserContext;

public class ServiceContextUserInfo implements UserInfo {

    @Override
    public Long getCurrentUserId() {
        LoginUserContext userContext = ServiceContext.getCurrentUser();
        if (userContext != null) {
            return userContext.getUserId();
        }
        return 0L;
    }
}
