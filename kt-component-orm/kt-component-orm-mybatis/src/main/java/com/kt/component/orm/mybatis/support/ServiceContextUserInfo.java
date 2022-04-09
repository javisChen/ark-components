package com.kt.component.orm.mybatis.support;

import com.kt.component.context.core.LoginUserContext;
import com.kt.component.context.core.ServiceContext;

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
