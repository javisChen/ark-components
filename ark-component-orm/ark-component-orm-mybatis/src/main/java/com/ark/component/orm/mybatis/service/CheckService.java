package com.ark.component.orm.mybatis.service;

import com.ark.component.exception.ExceptionFactory;
import com.ark.component.orm.mybatis.base.BaseEntity;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 通用的检查服务接口
 *
 * @param <T>
 */

public class CheckService<T extends BaseEntity> {

    @Autowired
    private IService<T> service;

    /**
     * 判断数据行是否不存在
     *
     * @param column 条件列
     * @param val    条件值
     * @return true=不存在 false=已存在
     */
    public boolean recordNotExists(SFunction<T, Object> column, Object val) {
        return recordNotExists(column, val, null);
    }

    /**
     * 判断数据行是否不存在，过滤id相同的行
     *
     * @param column    条件列
     * @param val       条件值
     * @param excludeId 排除的id
     * @return true=不存在 false=已存在
     */
    public boolean recordNotExists(SFunction<T, Object> column, Object val, Long excludeId) {
        return service.lambdaQuery()
                .eq(column, val)
                .ne(excludeId != null, BaseEntity::getId, excludeId)
                .count() == 0;
    }

    /**
     * 如果数据行存在会抛出异常
     *
     * @param column 条件列
     * @param val    条件值
     */
    public void ensureRecordNotExists(SFunction<T, Object> column, Object val) {
        ensureRecordNotExists(column, val, null, null);
    }

    /**
     * 如果数据行存在会抛出异常
     *
     * @param column 条件列
     * @param val    条件值
     * @param msg    指定抛出异常信息
     */
    public void ensureRecordNotExists(SFunction<T, Object> column, Object val, String msg) {
        ensureRecordNotExists(column, val, null, msg);
    }

    /**
     * 如果数据行存在会抛出异常
     *
     * @param column    条件列
     * @param val       条件值
     * @param excludeId 排除的id
     */
    public void ensureRecordNotExists(SFunction<T, Object> column, Object val, Long excludeId) {
        ensureRecordNotExists(column, val, excludeId, null);
    }

    /**
     * 如果数据行存在会抛出异常
     *
     * @param column    条件列
     * @param val       条件值
     * @param excludeId 排除的id
     * @param errorMsg  指定抛出异常信息
     */
    public void ensureRecordNotExists(SFunction<T, Object> column,
                                      Object val,
                                      Long excludeId,
                                      String errorMsg) {
        if (!recordNotExists(column, val, excludeId)) {
            throw ExceptionFactory.userException(errorMsg);
        }
    }

}
