package com.ark.component.common.util.assemble;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
@Setter
public class FieldAssembleConfig<T, S, ID> {

    /**
     * 是否执行的前置条件
     */
    private boolean condition;

    /**
     * 需要装配的数据集
     */
    private List<T> dataSet;

    /**
     * 查询需要数据的字段
     */
    private Function<T, ID> queryKeyFunction;

    /**
     * 查询数据的结果集
     */
    private Function<List<ID>, List<S>> resultSet;

    /**
     * 设置装配单个对象的字段
     */
    private BiConsumer<T, S> fieldConsumer;

    /**
     * 设置装配集合的字段
     */
    private BiConsumer<T, List<S>> collectionFieldConsumer;

    /**
     * 装配数据和填充数据的映射key
     */
    private Function<S, ID> mapKey;

    public static <T, S, ID> Multiple<T, S, ID> Multiple(List<T> dataSet, Function<List<ID>, List<S>> resultSet) {
        return new Multiple<>(dataSet, resultSet);
    }

    public static <T, S, ID> Multiple<T, S, ID> Multiple() {
        return new Multiple<>();
    }

    public static <T, S, ID> Single<T, S, ID> Single() {
        return new Single<>();
    }
    public static <T, S, ID> Single<T, S, ID> Single(List<T> dataSet, Function<List<ID>, List<S>> resultSet) {
        return new Single<>(dataSet, resultSet);
    }

    public static class Multiple<T, S, ID> {
        private boolean condition;

        private List<T> dataSet;

        private Function<T, ID> queryKeyFunction;

        private Function<List<ID>, List<S>> resultSet;

        private BiConsumer<T, List<S>> collectionField;

        private Function<S, ID> mapKey;

        public Multiple(List<T> dataSet, Function<List<ID>, List<S>> resultSet) {
            this.dataSet = dataSet;
            this.resultSet = resultSet;
        }

        public Multiple() {}

        public FieldAssembleConfig<T, S, ID> build() {
            FieldAssembleConfig<T, S, ID> config = new FieldAssembleConfig<>();
            config.setCondition(this.condition);
            config.setDataSet(this.dataSet);
            config.setQueryKeyFunction(this.queryKeyFunction);
            config.setResultSet(this.resultSet);
            config.setCollectionFieldConsumer(this.collectionField);
            config.setMapKey(this.mapKey);
            return config;
        }

        public Multiple<T, S, ID> condition(boolean condition) {
            this.condition = condition;
            return this;
        }

        public Multiple<T, S, ID> dataSet(List<T> dataSet) {
            this.dataSet = dataSet;
            return this;
        }

        public Multiple<T, S, ID> queryKey(Function<T, ID> queryKeyFunction) {
            this.queryKeyFunction = queryKeyFunction;
            return this;
        }

        public Multiple<T, S, ID> resultSet(Function<List<ID>, List<S>> resultSet) {
            this.resultSet = resultSet;
            return this;
        }

        public Multiple<T, S, ID> collectionField(BiConsumer<T, List<S>> collectionField) {
            this.collectionField = collectionField;
            return this;
        }

        public Multiple<T, S, ID> mapKey(Function<S, ID> mapKey) {
            this.mapKey = mapKey;
            return this;
        }

    }

    public static class Single<T, S, ID> {
        private boolean condition;

        private List<T> dataSet;

        private Function<T, ID> queryKeyFunction;

        private Function<List<ID>, List<S>> resultSet;

        private BiConsumer<T, S> field;

        private Function<S, ID> mapKey;


        public Single(List<T> dataSet, Function<List<ID>, List<S>> resultSet) {
            this.dataSet = dataSet;
            this.resultSet = resultSet;
        }

        public Single() {
        }

        public FieldAssembleConfig<T, S, ID> build() {
            FieldAssembleConfig<T, S, ID> config = new FieldAssembleConfig<>();
            config.setCondition(this.condition);
            config.setDataSet(this.dataSet);
            config.setQueryKeyFunction(this.queryKeyFunction);
            config.setResultSet(this.resultSet);
            config.setFieldConsumer(this.field);
            config.setMapKey(this.mapKey);
            return config;
        }

        public Single<T, S, ID> condition(boolean condition) {
            this.condition = condition;
            return this;
        }

        public Single<T, S, ID> dataSet(List<T> dataSet) {
            this.dataSet = dataSet;
            return this;
        }

        public Single<T, S, ID> queryKey(Function<T, ID> queryKeyFunction) {
            this.queryKeyFunction = queryKeyFunction;
            return this;
        }

        public Single<T, S, ID> resultSet(Function<List<ID>, List<S>> resultSet) {
            this.resultSet = resultSet;
            return this;
        }

        public Single<T, S, ID> field(BiConsumer<T, S> field) {
            this.field = field;
            return this;
        }

        public Single<T, S, ID> mapKey(Function<S, ID> mapKey) {
            this.mapKey = mapKey;
            return this;
        }

    }

}