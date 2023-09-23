package com.ark.component.common.util.assemble;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class FieldAssembleConfig<RECORD, SOURCE> {

    private Function<List<Long>, List<SOURCE>> datasourceFunc;

    private Function<? super SOURCE, Long> bindKeyFunc;

    private BiConsumer<RECORD, List<SOURCE>> setFunc;

    public FieldAssembleConfig(Function<List<Long>, List<SOURCE>> datasourceFunc) {
        this.datasourceFunc = datasourceFunc;
    }
}