package com.ark.component.common.util.assemble;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
@Setter
public class Item<SOURCE, TARGET> {

    private Function<List<Long>, List<TARGET>> targetQueryFunc;

    private Function<? super TARGET, Long> targetIdFunc;

    private BiConsumer<SOURCE, List<TARGET>> sourceConsumer;

    public static <T> ItemBuilder<T> newBuilder(T) {
        return new ItemBuilder<>();
    }

    public class ItemBuilder<RECORD, TARGET> {

        private Function<List<Long>, List<TARGET>> targetQueryFunc;

        private Function<? super TARGET, Long> targetIdFunc;

        private BiConsumer<SOURCE, List<TARGET>> sourceConsumer;
    }
}