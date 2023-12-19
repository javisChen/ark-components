package com.ark.component.common.util.assemble;

import java.util.function.Function;

/**
 * 查询接口
 *
 * @param <KEY> 关联key
 * @param <IN>  输入数据流
 * @param <RS>  外部关联数据
 */
public interface QueryExecutor<KEY, IN, RS> {



    /**
     *
     * @param consumer 从查询结果集中提取出关联的key
     */
    QueryResult<IN, RS> keyBy(Function<RS, KEY> consumer);

}
