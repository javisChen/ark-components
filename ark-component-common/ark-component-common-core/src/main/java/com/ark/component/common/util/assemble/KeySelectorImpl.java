package com.ark.component.common.util.assemble;

import cn.hutool.core.lang.Assert;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class KeySelectorImpl<KEY, IN> implements KeySelector<KEY, IN> {

    private final List<IN> ds;

    private final List<KEY> keys;

    private final Function<IN, KEY> keySelector;

    public KeySelectorImpl(List<IN> ds, Function<IN, KEY> keySelector) {
        this.ds = ds;
        this.keySelector = keySelector;
        // 把需要查询外联数据的外键提取出来
        this.keys = this.ds.stream().filter(Objects::nonNull).map(keySelector).toList();
        Assert.notNull(keys);
    }

    @Override
    public <RS> QueryExecutor<KEY, IN, RS> query(Function<List<KEY>, List<RS>> queryFunction) {
        return new QueryExecutorImpl<>(this.ds, this.keys, this.keySelector, queryFunction);
    }
}
