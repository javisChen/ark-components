package com.ark.component.orm.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ark.component.orm.mybatis.support.UserInfo;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

public class BaseFieldAutoFillObjectHandler implements MetaObjectHandler {

    private final UserInfo userInfo;

    public BaseFieldAutoFillObjectHandler(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        Long currentUserId = userInfo.getCurrentUserId();
        LocalDateTime now = LocalDateTime.now();
        strictInsertFill(metaObject, "creator", Long.class, currentUserId);
        strictInsertFill(metaObject, "modifier", Long.class, currentUserId);
        strictInsertFill(metaObject, "gmtCreate", LocalDateTime.class, now);
        strictInsertFill(metaObject, "gmtModified", LocalDateTime.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "modifier", Long.class, userInfo.getCurrentUserId());
        strictUpdateFill(metaObject, "gmtModified", LocalDateTime.class, LocalDateTime.now());
    }

}
