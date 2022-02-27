package com.kt.component.common.util.bean;

import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Bean转换器
 * 引用cglib的实现，性能较好。但如果追求极致性能，仍然建议使用setter。
 * @author victor
 */
public class BeanConvertor {

    public static <S, T> T copy(S src, Class<T> targetClazz) {
        T targetObj = null;
        try {
            targetObj = targetClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanCopier beanCopier = BeanCopier.create(src.getClass(), targetClazz, false);
        beanCopier.copy(src, targetObj , null);
        return targetObj;
    }

    public static <S, T> List<T> copyList(List<S> srcList, Class<T> targetClazz) {
        List<T> targetList = new ArrayList<>(srcList.size());
        srcList.forEach(s -> targetList.add(copy(s, targetClazz)));
        return targetList;
    }

}
