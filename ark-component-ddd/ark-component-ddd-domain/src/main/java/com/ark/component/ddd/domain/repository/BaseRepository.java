package com.ark.component.ddd.domain.repository;

import com.ark.component.ddd.domain.AggregateRoot;

import java.io.Serializable;
import java.util.List;

/**
 * 定义资源库接口
 * @param <AR> 聚合根
 * @param <ID> ID类型
 */
public interface BaseRepository<AR extends AggregateRoot, ID extends Serializable> {

    void persist(AR ar);

    void persistAll(List<AR> ars);

    void deleteById(ID id);

    void delete(AR ar);

    void deleteAll(List<AR> ars);

    AR byId(ID id);

    AR byIdOrThrowError(ID id);

    List<AR> byIds(List<ID> ids);

}
