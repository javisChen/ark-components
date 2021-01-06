//package com.kt.component.db.handelr;
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import org.apache.ibatis.reflection.MetaObject;
//
//import java.time.LocalDateTime;
//
//public class FieldAutoFillObjectHandler implements MetaObjectHandler {
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        this.strictInsertFill(metaObject, "gmtCreate", LocalDateTime.class, LocalDateTime.now());
//        this.strictInsertFill(metaObject, "gmtModified", LocalDateTime.class, LocalDateTime.now());
//        this.strictInsertFill(metaObject, "creator", Long.class, -1L);
//        this.strictInsertFill(metaObject, "modifier", Long.class, -1L);
//
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        this.strictInsertFill(metaObject, "gmtModified", LocalDateTime.class, LocalDateTime.now());
//        this.strictInsertFill(metaObject, "modifier", Long.class, -1L);
//    }
//
//}