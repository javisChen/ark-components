package com.ark.component.common.util.assemble;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FieldAssembleConfigBuilder<RECORD, SOURCE> {

    private Function<List<Long>, List<SOURCE>> datasourceFunc;

    private Function<? super SOURCE, Long> bindKeyFunc;

    private BiConsumer<RECORD, List<SOURCE>> setFunc;

    public FieldAssembleConfigBuilder(Function<List<Long>, List<SOURCE>> datasourceFunc,
                                      Function<? super SOURCE, Long> bindKeyFunc,
                                      BiConsumer<RECORD, List<SOURCE>> setFunc) {
        this.datasourceFunc = datasourceFunc;
        this.bindKeyFunc = bindKeyFunc;
        this.setFunc = setFunc;
    }

    public Function<List<Long>, List<SOURCE>> getDatasourceFunc() {
        return datasourceFunc;
    }

    public Function<? super SOURCE, Long> getBindKeyFunc() {
        return bindKeyFunc;
    }

    public BiConsumer<RECORD, List<SOURCE>> getSetFunc() {
        return setFunc;
    }
}