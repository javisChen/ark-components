package com.ark.component.common.util.assemble;

import java.util.function.Function;

/**
 * 数据来源
 *
 * @param <S> 数据源
 */
public interface DataSource<S> {

    /**
     * 需要查询外联数据的
     *
     * @param keySelector key选择器
     * @param <K>        查询外部数据的K
     * @return KeySelector
     */
    <K> KeySelector<K, S> keySelect(Function<S, K> keySelector);
}
