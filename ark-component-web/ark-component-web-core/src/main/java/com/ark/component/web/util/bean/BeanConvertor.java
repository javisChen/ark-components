package com.ark.component.web.util.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ark.component.dto.PageResponse;
import com.ark.component.exception.ExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public static <S, T> List<T> copyList(List<S> srcList, Class<T> targetClazz) {
        if (CollectionUtils.isEmpty(srcList)) {
            return Collections.emptyList();
        }
        List<T> targetList = new ArrayList<>(srcList.size());
        srcList.forEach(s -> targetList.add(copy(s, targetClazz)));
        return targetList;
    }

    public static <S, T> PageResponse<T> copyPage(IPage<S> page, Class<T> targetClazz) {
        List<S> srcList = page.getRecords();
        List<T> targetList = null;
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

}
