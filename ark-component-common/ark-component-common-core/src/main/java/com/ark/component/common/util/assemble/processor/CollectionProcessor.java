package com.ark.component.common.util.assemble.processor;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CollectionProcessor<K, S, RS> implements ICollectionProcessor<S, RS> {

    private final List<S> sources;

    private final Map<K, List<RS>> keyed;

    private final Function<S, K> keySelector;


    public CollectionProcessor(Function<S, K> keySelector, List<S> sources, Map<K, List<RS>> keyed) {
        this.sources = sources;
        this.keySelector = keySelector;
        this.keyed = keyed;
    }

    @Override
    public void process(BiConsumer<S, List<RS>> processConsumer) {
        if (keyed == null) {
            return;
        }
        for (S data : sources) {
            K k = keySelector.apply(data);
            List<RS> rs = keyed.get(k);
            if (CollectionUtils.isNotEmpty(rs)) {
                processConsumer.accept(data, rs);
            }
        }
    }
}
