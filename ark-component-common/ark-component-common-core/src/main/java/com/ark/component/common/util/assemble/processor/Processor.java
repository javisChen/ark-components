package com.ark.component.common.util.assemble.processor;

import java.util.function.BiConsumer;

/**
 * 数据加工器
 *
 * @param <S>  数据源
 * @param <RS> 外联数据结果集
 */
public interface Processor<S, RS> {

    /**
     * 数据处理
     *
     * @param processConsumer 提供数据源和外联数据结果集
     */
    void process(BiConsumer<S, RS> processConsumer);

}
