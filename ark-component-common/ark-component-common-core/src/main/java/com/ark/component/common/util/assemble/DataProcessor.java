package com.ark.component.common.util.assemble;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Function;

public class DataProcessor<S> {

    private final List<S> sources;

    public DataProcessor(S source) {
        this.sources = Lists.newArrayList(source);
    }

    public DataProcessor(List<S> sources) {
        this.sources = sources;
    }


    public <K> KeySelector<K, S> keySelect(Function<S, K> keySelector) {
        return new CollectionDataSource<>(sources).keySelect(keySelector);
    }

}
