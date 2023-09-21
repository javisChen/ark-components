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
    private Function<? super RECORD, Long> idFunc;


    private List<Item<RECORD, TARGET>> items;



    public AssemblerBuilder<RECORD, TARGET> records(List<RECORD> records) {
        this.records = records;
        return this;
    }

    public AssemblerBuilder<RECORD, TARGET> idFunc(Function<? super RECORD, Long> idFunc) {
        this.idFunc = idFunc;
        return this;
    }

    public AssemblerBuilder<RECORD, TARGET> items(Function<Item.ItemBuilder<RECORD, TARGET>, List<Item<RECORD, TARGET>>> builderConsumer) {
        Item.ItemBuilder<RECORD, TARGET> itemBuilder = new Item.ItemBuilder<>();
        this.items = builderConsumer.apply(itemBuilder);
        return this;
    }

    public AssembleHelper<RECORD, TARGET> build() {
        AssembleHelper<RECORD, TARGET> assembleHelper = new AssembleHelper<>();
        assembleHelper.setRecords(this.records);
        assembleHelper.setIdFunc(this.idFunc);
        assembleHelper.setItems(this.items);
        return assembleHelper;
    }


}
