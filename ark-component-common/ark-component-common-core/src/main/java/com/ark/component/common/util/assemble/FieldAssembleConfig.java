package com.ark.component.common.util.assemble;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
@Setter
public class FieldAssembleConfig<RECORD, SOURCE> {

    private boolean condition;

    private List<RECORD> records;

    private Function<? super RECORD, Long> recordId;

    private Function<List<Long>, List<SOURCE>> datasource;

    private BiConsumer<RECORD, List<SOURCE>> field;

    private Function<? super SOURCE, Long> bindingKey;

    public static <RECORD, SOURCE> Builder<RECORD, SOURCE> builder() {
        return new Builder<>();
    }
    
    public static class Builder<RECORD, SOURCE> {
        private boolean condition;

        private List<RECORD> records;

        private Function<? super RECORD, Long> recordId;

        private Function<List<Long>, List<SOURCE>> datasource;

        private BiConsumer<RECORD, List<SOURCE>> field;

        private Function<? super SOURCE, Long> bindingKey;

        public Builder() {}

        public FieldAssembleConfig<RECORD, SOURCE> build() {
            FieldAssembleConfig<RECORD, SOURCE> config = new FieldAssembleConfig<>();
            config.setCondition(this.condition);
            config.setRecords(this.records);
            config.setRecordId(this.recordId);
            config.setDatasource(this.datasource);
            config.setField(this.field);
            config.setBindingKey(this.bindingKey);
            return config;
        }

        public Builder<RECORD, SOURCE> condition(boolean condition) {
            this.condition = condition;
            return this;
        }

        public Builder<RECORD, SOURCE> records(List<RECORD> records) {
            this.records = records;
            return this;
        }

        public Builder<RECORD, SOURCE> recordId(Function<? super RECORD, Long> recordId) {
            this.recordId = recordId;
            return this;
        }

        public Builder<RECORD, SOURCE> datasource(Function<List<Long>, List<SOURCE>> datasource) {
            this.datasource = datasource;
            return this;
        }

        public Builder<RECORD, SOURCE> field(BiConsumer<RECORD, List<SOURCE>> field) {
            this.field = field;
            return this;
        }

        public Builder<RECORD, SOURCE> bindingKey(Function<? super SOURCE, Long> bindingKey) {
            this.bindingKey = bindingKey;
            return this;
        }
    }

}