package com.ark.component.web.util.separate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 数据分离器
 */
public class DataSeparator {

    /**
     * 从两个集合中识别出新增/更新的数据
     *
     * @param d1      新数据集合
     * @param d2      原有数据集合
     * @param keyFunc key
     * @return 返回update和insert
     */
    public static <T, KEY> SeparateResult<T> separate(List<T> d1, List<T> d2, Function<T, KEY> keyFunc) {
        List<T> inserts = new ArrayList<>(d1.size());
        List<T> updates = new ArrayList<>(d1.size());
        Map<KEY, T> d2Map = d2.stream().collect(Collectors.toMap(keyFunc, Function.identity()));
        for (T e : d1) {
            if (d2Map.containsKey(keyFunc.apply(e))) {
                updates.add(e);
            } else {
                inserts.add(e);
            }
        }
        return new SeparateResult<>(inserts, updates);
    }


}

