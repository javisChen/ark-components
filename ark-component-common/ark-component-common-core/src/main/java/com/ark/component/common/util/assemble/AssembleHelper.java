package com.ark.component.common.util.assemble;

import cn.hutool.core.collection.CollUtil;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Setter
public class AssembleHelper<RECORD, TARGET> {

    private List<RECORD> records;

    private Function<? super RECORD, Long> idFunc;

    private List<FieldAssembleConfigBuilder<RECORD, TARGET>> fieldAssembleConfigBuilders;

    public static void main(String[] args) {
    }

    public void execute() {
        List<Long> groupIds = CollUtil.map(this.records, this.idFunc, true);
        if (CollectionUtils.isEmpty(groupIds)) {
            return;
        }
        for (FieldAssembleConfigBuilder<RECORD, TARGET> fieldAssembleConfigBuilder : fieldAssembleConfigBuilders) {
            Function<? super TARGET, Long> targetIdFunc = fieldAssembleConfigBuilder.getBindKeyFunc();
            Function<List<Long>, List<TARGET>> targetQueryFunc = fieldAssembleConfigBuilder.getDatasourceFunc();
            BiConsumer<RECORD, List<TARGET>> sourceConsumer = fieldAssembleConfigBuilder.getSetFunc();
            List<TARGET> targets = targetQueryFunc.apply(groupIds);
            if (CollectionUtils.isEmpty(targets)) {
                return;
            }
            Map<Long, List<TARGET>> map = targets.stream().collect(Collectors.groupingBy(targetIdFunc));
            if (MapUtils.isEmpty(map)) {
                return;
            }
            this.records.forEach(record -> {
                List<TARGET> targetList = map.get(this.idFunc.apply(record));
                sourceConsumer.accept(record, targetList);
            });
        }
    }

    public static <T, R> AssemblerBuilder<T, R> newBuilder(List<T> records) {
        return new AssemblerBuilder<>();
    }
}
