package com.ark.component.orm.mybatis.support;

import com.ark.component.context.core.ServiceContext;
import com.ark.component.security.base.user.LoginUser;

public class ServiceContextUserInfo implements UserInfo {

    @Override
    public Long getCurrentUserId() {
        LoginUser loginUser = ServiceContext.getCurrentUser();
        if (loginUser != null) {
            return loginUser.getUserId();
        }
        return 0L;
    }
}
