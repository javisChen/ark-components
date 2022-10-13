package com.ark.component.orm.mybatis.support;

/**
 * 默认实现
 * @author jc
 */
public class DefaultUserInfo implements UserInfo {

    @Override
    public Long getCurrentUserId() {
        return 0L;
    }

}
