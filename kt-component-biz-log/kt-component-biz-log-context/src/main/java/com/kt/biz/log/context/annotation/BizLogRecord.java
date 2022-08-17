package com.kt.biz.log.context.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BizLogRecord {

    /**
     * 操作成功模板
     */
    String success();

    String fail() default "";

    /**
     * 操作人，默认是当前登录用户名称
     */
    String operator() default "";

    String bizCode() default "";

    /**
     * 业务日志会有对应的关联的业务id
     */
    String bizId() default "";

    /**
     * 日志类型
     */
    String type();

    /**
     * 节点显示
     */
    String nodeText();

    /**
     * 跟进记录附件字段名称
     */
    String fileFieldName() default "";

    /**
     * 附件类型
     */
    String fileType() default "";

    String detail() default "";

    /**
     * 根据条件判断是否需要记录日志
     */
    String condition() default "";

    /**
     * 扩展信息，存什么都行
     */
    String extension() default "";

}