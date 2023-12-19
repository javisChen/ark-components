package com.ark.component.common.util.assemble;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FieldsAssembler {

    public static <T, S, ID> void fill(FieldAssembleConfig<T, S, ID> config) {
        fill(config.isCondition(),
                config.getDataSet(),
                config.getQueryKeyFunction(),
                config.getResultSet(),
                config.getCollectionFieldConsumer(),
                config.getMapKey());
    }

    public static <T, S, ID> void fillSingle(FieldAssembleConfig<T, S, ID> config) {
        fillSingle(config.isCondition(),
                config.getDataSet(),
                config.getQueryKeyFunction(),
                config.getResultSet(),
                config.getFieldConsumer(),
                config.getMapKey());
    }

    /**
     * 装配字段
     *
     * @param condition         判断是否需要进行装配
     * @param dataSet          需要装配的数据集合
     * @param queryKeyFunction  数据行id
     * @param field             需要装配的字段
     * @param resultSetFunction 数据源
     * @param mapKey            与records关联的字段
     * @param <T>               需要被装配的数据
     * @param <S>               待装配的数据源
     */
    private static <T, S, ID> void fill(boolean condition,
                                        List<T> dataSet,
                                        Function<? super T, ID> queryKeyFunction,
                                        Function<List<ID>, List<S>> resultSetFunction,
                                        BiConsumer<T, List<S>> field,
                                        Function<? super S, ID> mapKey) {
        if (!condition) {
            return;
        }
        List<ID> groupIds = CollUtil.map(dataSet, queryKeyFunction, true);
        if (CollectionUtils.isEmpty(groupIds)) {
            return;
        }
        List<S> targets = resultSetFunction.apply(groupIds);
        if (CollectionUtils.isEmpty(targets)) {
            return;
        }
        Map<ID, List<S>> map = targets.stream().collect(Collectors.groupingBy(mapKey));
        if (MapUtils.isEmpty(map)) {
            return;
        }
        dataSet.forEach(t -> field.accept(t, map.get(queryKeyFunction.apply(t))));
    }


    /**
     * 装配字段
     *
     * @param condition         判断是否需要进行装配
     * @param dataSet          需要装配的数据集合
     * @param queryKeyFunction  数据行id
     * @param field             需要装配的字段
     * @param resultSetFunction 数据源
     * @param <T>               需要被装配的数据
     * @param <S>               待装配的数据源
     */
    private static <T, S, ID> void fillSingle(boolean condition,
                                              List<T> dataSet,
                                              Function<T, ID> queryKeyFunction,
                                              Function<List<ID>, List<S>> resultSetFunction,
                                              BiConsumer<T, S> field,
                                              Function<S, ID> mapKey) {
        if (!condition) {
            return;
        }
        List<ID> groupIds = CollUtil.map(dataSet, queryKeyFunction, true);
        if (CollectionUtils.isEmpty(groupIds)) {
            return;
        }

        List<S> targets = resultSetFunction.apply(groupIds);
        if (CollectionUtils.isEmpty(targets)) {
            return;
        }
        Map<ID, S> map = targets.stream().collect(Collectors.toMap(mapKey, Function.identity()));
        dataSet.forEach(record -> {
            S source = map.get(queryKeyFunction.apply(record));
            field.accept(record, source);
        });
    }

}

