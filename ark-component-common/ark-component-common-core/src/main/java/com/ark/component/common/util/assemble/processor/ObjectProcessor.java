package com.ark.component.common.util.assemble.processor;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ObjectProcessor<K, S, RS> implements Processor<S, RS> {

    private final List<S> sources;

    private final Map<K, RS> keyed;

    private final Function<S, K> keySelector;

    public ObjectProcessor(List<S> sources, Function<S, K> keySelector, Map<K, RS> keyed) {
        this.sources = sources;
        this.keyed = keyed;
        this.keySelector = keySelector;
    }


    @Override
    public void process(BiConsumer<S, RS> processConsumer) {
        if (keyed == null) {
            return;
        }
        for (S source : sources) {
            K key = keySelector.apply(source);
            RS rs = keyed.get(key);
            if (rs != null) {
                processConsumer.accept(source, rs);
            }
        }
    }
}
