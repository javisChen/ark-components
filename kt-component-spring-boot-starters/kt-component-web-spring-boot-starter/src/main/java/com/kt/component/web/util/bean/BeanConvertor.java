package com.kt.component.web.util.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kt.component.dto.PageResponse;
import com.kt.component.exception.ExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean转换器
 * 引用cglib的实现，性能较好。但如果追求极致性能，仍然建议使用setter。
 * @author victor
 */
@Slf4j
public class BeanConvertor {

    public static <S, T> T copy(S src, Class<T> targetClazz) {
        T targetObj;
        try {
            targetObj = targetClazz.newInstance();
        } catch (Exception e) {
            log.error("bean转换异常", e);
            throw ExceptionFactory.sysException("bean转换异常");
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

    public static <S, T> PageResponse<T> copyPage(IPage<S> page, Class<T> targetClazz) {
        List<S> srcList = page.getRecords();
        List<T> targetList = copyList(srcList, targetClazz);
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setTotal(page.getTotal());
        pageResponse.setSize(page.getSize());
        pageResponse.setCurrent(page.getCurrent());
        pageResponse.setRecords(targetList);
        return pageResponse;
    }

}
