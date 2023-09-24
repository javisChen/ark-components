package com.ark.component.common.util.assemble;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FieldAssembleConfigBuilder<RECORD, SOURCE> {

    private Function<List<Long>, List<SOURCE>> datasourceFunc;

    private Function<? super SOURCE, Long> bindKeyFunc;

    private BiConsumer<RECORD, List<SOURCE>> setFunc;

    private AssemblerBuilder<RECORD, SOURCE> parent;

    public FieldAssembleConfigBuilder(AssemblerBuilder<RECORD, SOURCE> assemblerBuilder) {
        this.parent = assemblerBuilder;
    }

    public AssemblerBuilder<RECORD, SOURCE> and() {
        return this.parent;
    }


    public FieldAssembleConfigBuilder<RECORD, SOURCE> datasourceFunc(Function<List<Long>, List<SOURCE>> datasourceFunc) {
        this.datasourceFunc = datasourceFunc;
        return this;
    }

    public FieldAssembleConfigBuilder<RECORD, SOURCE> bindKeyFunc(Function<? super SOURCE, Long> bindKeyFunc) {
        this.bindKeyFunc = bindKeyFunc;
        return this;
    }

    public FieldAssembleConfigBuilder<RECORD, SOURCE> setFunc(BiConsumer<RECORD, List<SOURCE>> setFunc) {
        this.setFunc = setFunc;
        return this;
    }
}