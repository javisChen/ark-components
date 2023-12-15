package com.ark.component.common.util.assemble.v2;

import cn.hutool.core.collection.CollUtil;
import com.ark.component.common.util.assemble.FieldAssembleConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FieldsAssemblerV2 {


//    /**
//     * 装配字段
//     *
//     * @param records    需要装配的数据集合
//     * @param recordId   数据行id
//     * @param field      需要装配的字段
//     * @param datasource 数据源
//     * @param bindingKey 与records关联的字段
//     */
//    public static <RECORD, SOURCE> void execute(List<RECORD> records,
//                                                Function<? super RECORD, Long> recordId,
//                                                Function<List<Long>, List<SOURCE>> datasource,
//                                                BiConsumer<RECORD, List<SOURCE>> field,
//                                                Function<? super SOURCE, Long> bindingKey) {
//        execute(true, records, recordId, datasource, field, bindingKey);
//    }


    public static <RECORD, SOURCE> void execute(FieldAssembleConfig<RECORD, SOURCE> config) {
        execute(config.isCondition(),
                config.getRecords(),
                config.getRecordId(),
                config.getDatasource(),
                config.getField(),
                config.getBindingKey());
    }


    /**
     * 装配字段
     *
     * @param condition  判断是否需要进行装配
     * @param records    需要装配的数据集合
     * @param recordId   数据行id
     * @param field      需要装配的字段
     * @param datasource 数据源
     * @param bindingKey 与records关联的字段
     * @param <RECORD>   需要被装配的数据
     * @param <SOURCE>   待装配的数据源
     */
    private static <RECORD, SOURCE> void execute(boolean condition,
                                                List<RECORD> records,
                                                Function<? super RECORD, Long> recordId,
                                                Function<List<Long>, List<SOURCE>> datasource,
                                                BiConsumer<RECORD, List<SOURCE>> field,
                                                Function<? super SOURCE, Long> bindingKey) {
        if (!condition) {
            return;
        }
        List<Long> groupIds = CollUtil.map(records, recordId, true);
        if (CollectionUtils.isEmpty(groupIds)) {
            return;
        }
        List<SOURCE> targets = datasource.apply(groupIds);
        if (CollectionUtils.isEmpty(targets)) {
            return;
        }
        Map<Long, List<SOURCE>> map = targets.stream().collect(Collectors.groupingBy(bindingKey));
        if (MapUtils.isEmpty(map)) {
            return;
        }
        records.forEach(record -> field.accept(record, map.get(recordId.apply(record))));
    }

//    /**
//     * 装配字段
//     *
//     * @param record     需要装配的数据
//     * @param recordId   数据行id
//     * @param field      需要装配的字段
//     * @param datasource 数据源
//     * @param <RECORD>   需要被装配的数据
//     * @param <SOURCE>   待装配的数据源
//     */
//    public static <RECORD, SOURCE> void execute(RECORD record,
//                                                Function<? super RECORD, Long> recordId,
//                                                BiConsumer<RECORD, SOURCE> field,
//                                                Function<Long, SOURCE> datasource) {
//        execute(true, record, recordId, field, datasource);
//    }

    /**
     * 装配字段
     * @param condition  判断是否需要进行装配
     * @param record     需要装配的数据
     * @param recordId   数据行id
     * @param field      需要装配的字段
     * @param datasource 数据源
     * @param <RECORD>   需要被装配的数据
     * @param <SOURCE>   待装配的数据源
     */
    public static <RECORD, SOURCE> void execute(boolean condition,
                                                RECORD record,
                                                Function<? super RECORD, Long> recordId,
                                                BiConsumer<RECORD, SOURCE> field,
                                                Function<Long, SOURCE> datasource) {
        if (record == null) {
            return;
        }
        Long id = recordId.apply(record);

        SOURCE target = datasource.apply(id);
        if (Objects.isNull(target)) {
            return;
        }
        field.accept(record, target);
    }

}

