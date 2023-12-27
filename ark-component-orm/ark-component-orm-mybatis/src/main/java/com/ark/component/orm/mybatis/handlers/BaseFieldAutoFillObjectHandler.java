package com.ark.component.orm.mybatis.handlers;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ark.component.orm.mybatis.support.UserInfo;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

public class BaseFieldAutoFillObjectHandler implements MetaObjectHandler {


    private final static String COLUMN_CREATOR = "creator";
    private final static String COLUMN_MODIFIER = "modifier";
    private final static String COLUMN_GMT_CREATE = "gmtCreate";
    private final static String COLUMN_GMT_MODIFIED = "gmtModified";

    private final UserInfo userInfo;

    public BaseFieldAutoFillObjectHandler(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        Long currentUserId = userInfo.getCurrentUserId();
        LocalDateTime now = LocalDateTime.now();
        strictInsertFill(metaObject, COLUMN_CREATOR, Long.class, currentUserId);
        strictInsertFill(metaObject, COLUMN_MODIFIER, Long.class, currentUserId);
        strictInsertFill(metaObject, COLUMN_GMT_CREATE, LocalDateTime.class, now);
        strictInsertFill(metaObject, COLUMN_GMT_MODIFIED, LocalDateTime.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, COLUMN_MODIFIER, Long.class, userInfo.getCurrentUserId());
        strictUpdateFill(metaObject, COLUMN_GMT_MODIFIED, LocalDateTime.class, LocalDateTime.now());
    }

}
