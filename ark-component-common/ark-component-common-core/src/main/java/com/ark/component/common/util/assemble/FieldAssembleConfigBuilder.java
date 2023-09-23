package com.ark.component.common.util.assemble;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
@Setter
public class FieldAssembleConfigBuilder<RECORD, SOURCE> {

    private Function<List<Long>, List<SOURCE>> datasourceFunc;

    private Function<? super SOURCE, Long> bindKeyFunc;

    private BiConsumer<RECORD, List<SOURCE>> setFunc;

    private List<FieldAssembleConfig<RECORD, SOURCE>> configs = new ArrayList<>();

    public FieldAssembleConfigBuilder<RECORD, SOURCE> and() {
        return this;
    }
    public List<FieldAssembleConfig<RECORD, SOURCE>> build() {
        return configs;
    }

    public FieldAssembleConfig<RECORD, SOURCE> item(Function<List<Long>, List<SOURCE>> datasourceFunc) {
        FieldAssembleConfig<RECORD, SOURCE> e = new FieldAssembleConfig<>(datasourceFunc);
        configs.add(e);
        return e;
    }

}