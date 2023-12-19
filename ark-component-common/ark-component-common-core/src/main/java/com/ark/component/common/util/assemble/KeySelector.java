package com.ark.component.common.util.assemble;

import java.util.List;
import java.util.function.Function;

/**
 * key选择器
 *
 * @param <K> 查询外部数据的key
 * @param <S> 数据源
 */
public interface KeySelector<K, S> {

    /**
     * 根据关联key查询需要外联数据集
     *
     * @param queryFunction 查询方法实现
     * @param <R>           查询外部数据结果集
     * @return QueryExecutor
     */
    <R> QueryExecutor<K, S, R> query(Function<List<K>, List<R>> queryFunction);
}
