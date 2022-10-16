package com.ark.component.web.util.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ark.component.dto.PageResponse;
import com.ark.component.exception.ExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.*;

/**
 * Bean转换器
 * 引用cglib的实现，性能较好。但如果追求极致性能，仍然建议使用setter。
 * @author victor
 */
@Slf4j
public class BeanConvertor {

    public static <S, T> T copy(S src, Class<T> targetClazz) {
        if (Objects.isNull(src)) {
            return null;
        }
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

    public static <S, T> Collection<T> copyList(Collection<S> srcList, Class<T> targetClazz) {
        if (CollectionUtils.isEmpty(srcList)) {
            return Collections.emptyList();
        }
        Collection<T> targetList = new ArrayList<>(srcList.size());
        srcList.forEach(s -> targetList.add(copy(s, targetClazz)));
        return targetList;
    }

    public static <S, T> PageResponse<T> copyPage(IPage<S> page, Class<T> targetClazz) {
        return toPage(page, targetClazz);
    }

    private static <S, T> PageResponse<T> toPage(IPage<S> page, Class<T> targetClazz) {
        Collection<S> srcList = page.getRecords();
        Collection<T> targetList = null;
        if (CollectionUtils.isNotEmpty(srcList)) {
            targetList = copyList(srcList, targetClazz);
        } else {
            targetList = Collections.emptyList();
        }
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setTotal((int) page.getTotal());
        pageResponse.setSize((int) page.getSize());
        pageResponse.setCurrent((int) page.getCurrent());
        pageResponse.setRecords(targetList);
        return pageResponse;
    }

    public static <S, T> PageResponse<T> copyPage(PageResponse<S> page, Class<T> targetClazz) {
        Collection<S> srcList = page.getRecords();
        Collection<T> targetList = null;
        if (CollectionUtils.isNotEmpty(srcList)) {
            targetList = copyList(srcList, targetClazz);
        } else {
            targetList = Collections.emptyList();
        }
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setTotal(page.getTotal());
        pageResponse.setSize(page.getSize());
        pageResponse.setCurrent(page.getCurrent());
        pageResponse.setRecords(targetList);
        return pageResponse;
    }

}
