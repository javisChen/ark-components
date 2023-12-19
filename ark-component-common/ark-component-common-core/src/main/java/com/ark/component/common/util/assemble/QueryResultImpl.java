package com.ark.component.common.util.assemble;

import com.ark.component.common.util.assemble.processor.CollectionProcessor;
import com.ark.component.common.util.assemble.processor.ICollectionProcessor;
import com.ark.component.common.util.assemble.processor.ObjectProcessorImpl;
import com.ark.component.common.util.assemble.processor.Processor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QueryResultImpl<K, S, RS> implements QueryResult<S, RS> {

    private final List<S> sources;
    private final List<RS> resultSet;
    private final Function<S, K> keySelector;
    private final Function<RS, K> resultKeySelector;

    public QueryResultImpl(List<S> sources, List<RS> resultSet, Function<S, K> keySelector, Function<RS, K> resultKeySelector) {
        this.sources = sources;
        this.resultSet = resultSet;
        this.keySelector = keySelector;
        this.resultKeySelector = resultKeySelector;
    }

    @Override
    public ICollectionProcessor<S, RS> collection() {
        Map<K, List<RS>> keyed = null;
        if (CollectionUtils.isNotEmpty(resultSet)) {
            keyed = resultSet.stream().collect(Collectors.groupingBy(resultKeySelector));
        }
        return new CollectionProcessor<>(keySelector, sources, keyed);
    }

    @Override
    public Processor<S, RS> object() {
        Map<K, RS> keyed = null;
        if (CollectionUtils.isNotEmpty(resultSet)) {
            keyed = resultSet.stream().collect(Collectors.toMap(resultKeySelector, Function.identity()));
        }
        return new ObjectProcessorImpl<>(sources, keySelector, keyed);
    }
}
