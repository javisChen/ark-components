package com.ark.component.common.util.assemble;

import java.util.List;
import java.util.function.Function;

public class QueryExecutorImpl<K, S, RS> implements QueryExecutor<K, S, RS> {

    private final List<S> sources;
    private final List<RS> resultSet;
    private final Function<S, K> keySelector;

    public QueryExecutorImpl(List<S> sources,
                             List<K> keys,
                             Function<S, K> keySelector,
                             Function<List<K>, List<RS>> function) {
        this.sources = sources;
        this.keySelector = keySelector;
        // 执行查询方法获取外联数据结果集
        this.resultSet = function.apply(keys);
    }

    @Override
    public QueryResult<S, RS> keyBy(Function<RS, K> consumer) {
        return new QueryResultImpl<>(sources, resultSet, keySelector, consumer);
    }
}
