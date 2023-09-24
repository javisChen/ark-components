package com.ark.component.common.util.assemble;

import java.util.List;
import java.util.function.Function;

public class AssemblerBuilder<RECORD, TARGET> {


    /**
     * 待装配的数据集
     */
    private List<RECORD> records;

    /**
     * 待装配数据集的id获取
     */
    private Function<? super RECORD, Long> id;


    private List<FieldAssembleConfigBuilder<RECORD, TARGET>> fieldAssembleConfigBuilders;



    public AssemblerBuilder<RECORD, TARGET> records(List<RECORD> records) {
        this.records = records;
        return this;
    }

    public AssemblerBuilder<RECORD, TARGET> id(Function<? super RECORD, Long> id) {
        this.id = id;
        return this;
    }

    public FieldAssembleConfigBuilder<RECORD, TARGET> with() {
        FieldAssembleConfigBuilder<RECORD, TARGET> itemBuilder = new FieldAssembleConfigBuilder<>(this);
        return itemBuilder;
    }

    public AssembleHelper<RECORD, TARGET> build() {
        AssembleHelper<RECORD, TARGET> assembleHelper = new AssembleHelper<>();
        assembleHelper.setRecords(this.records);
        assembleHelper.setIdFunc(this.id);
        assembleHelper.setFieldAssembleConfigBuilders(this.fieldAssembleConfigBuilders);
        return assembleHelper;
    }

}
