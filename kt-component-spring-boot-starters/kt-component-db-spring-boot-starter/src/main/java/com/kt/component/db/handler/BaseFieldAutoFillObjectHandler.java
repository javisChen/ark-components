package com.kt.component.db.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.kt.component.context.LoginUserContext;
import com.kt.component.context.ServiceContext;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

public class BaseFieldAutoFillObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Long currentUserId = getCurrentUserId();
        strictInsertFill(metaObject, "creator", Long.class, currentUserId);
        strictInsertFill(metaObject, "modifier", Long.class, currentUserId);
        strictInsertFill(metaObject, "gmtCreate", LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, "gmtModified", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long currentUserId = getCurrentUserId();
        strictUpdateFill(metaObject, "modifier", Long.class, currentUserId);
        strictUpdateFill(metaObject, "gmtModified", LocalDateTime.class, LocalDateTime.now());
    }

    private Long getCurrentUserId() {
        Long currentUserId = 0L;
        LoginUserContext currentUser = ServiceContext.getCurrentUser();
        if (Objects.nonNull(currentUser)) {
            currentUserId = currentUser.getUserId();
        }
        return currentUserId;
    }

}
