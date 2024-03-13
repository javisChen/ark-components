package com.ark.component.ddd.domain.repository;

import com.ark.component.ddd.domain.AggregateRoot;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;

/**
 * 定义资源库接口
 *
 * @param <AR> 聚合根
 * @param <ID> ID类型
 */
public interface BaseRepository<AR extends AggregateRoot, ID extends Serializable> {

    void saveAndPublishEvents(AR ar);

    void saveAndPublishEvents(List<AR> ars);

    void deleteAndPublishEvents(AR ar);

    void deleteAndPublishEvents(List<AR> ars);

    void deleteAll(List<AR> ars);

    AR byId(ID id);

    AR byIdOrThrowError(ID id);

    <X extends Throwable> AR byIdOrThrowError(ID id, Supplier<X> throwable) throws X;

    AR byIdOrThrowError(ID id, String msg);

    default List<AR> byIds(List<ID> ids) {
        return null;
    }

}
