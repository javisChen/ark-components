package com.ark.component.common.util.assemble;

import java.util.List;
import java.util.function.Function;

public class CollectionDataSource<S> implements DataSource<S> {

    private final List<S> sources;
    public CollectionDataSource(List<S> sources) {
        this.sources = sources;
    }

    @Override
    public <K> KeySelector<K, S> keySelect(Function<S, K> keySelector) {
        return new KeySelectorImpl<>(sources, keySelector);
    }
}
