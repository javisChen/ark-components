package com.ark.component.ddd.infrastructure;

import com.ark.component.ddd.domain.AggregateRoot;
import com.ark.component.ddd.domain.repository.BaseRepository;
import com.ark.component.ddd.domain.event.DomainEvent;
import com.ark.component.ddd.domain.event.DomainEventDao;
import com.ark.component.ddd.infrastructure.event.ThreadLocalDomainEventIdHolder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.*;

import static cn.hutool.core.lang.Assert.notNull;
import static java.util.Collections.emptyList;
import static java.util.List.copyOf;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;


/**
 * 数据库实现资源库的抽象
 * @param <AR>
 */
public class BaseDBRepository<AR extends AggregateRoot, ID extends Serializable> extends ServiceImpl<BaseMapper<AR>, AR> implements BaseRepository<AR, ID> {

    @Autowired
    private DomainEventDao domainEventDao;

    @Override
    public void persist(AR ar) {
        requireNonNull(ar, "AR must not be null.");

        if (!isEmpty(ar.getEvents())) {
            saveEvents(ar.getEvents());
            ar.clearEvents();
        }

        saveOrUpdate(ar);
    }

    @Override
    public void persistAll(List<AR> ars) {
        if (isEmpty(ars)) {
            return;
        }

        List<DomainEvent> events = new ArrayList<>();
        ars.forEach(ar -> {
            if (!isEmpty(ar.getEvents())) {
                events.addAll(ar.getEvents());
                ar.clearEvents();
            }
            super.saveOrUpdate(ar);
        });

        saveEvents(events);
    }

    @Override
    public void deleteById(ID id) {
        removeById(id);
    }

    @Override
    public void delete(AR it) {
        requireNonNull(it, "AR must not be null.");

        if (!isEmpty(it.getEvents())) {
            saveEvents(it.getEvents());
            it.clearEvents();
        }
        removeById(it);
    }

    @Override
    public void deleteAll(List<AR> ars) {
        if (isEmpty(ars)) {
            return;
        }

        List<DomainEvent> events = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        ars.forEach(ar -> {
            if (!isEmpty(ar.getEvents())) {
                events.addAll(ar.getEvents());
                ar.clearEvents();
            }
            ids.add(ar.getId());
        });

        saveEvents(events);

        removeByIds(ars.stream().map(AR::getId).toList());
    }

    @Override
    public AR byId(ID id) {
        notNull(id, "AR ID must not be null.");
        return getById(id);
    }

    @Override
    public AR byIdOrThrowError(ID id) {
        AR ar = byId(id);
        notNull(ar, "Cannot find the ar.");
        return ar;
    }

    @Override
    public List<AR> byIds(List<ID> ids) {
        if (isEmpty(ids)) {
            return emptyList();
        }
        List<AR> ars = listByIds(ids);
        return copyOf(ars);
    }


    private void saveEvents(List<DomainEvent> events) {
        if (!isEmpty(events)) {
            domainEventDao.insert(events);
            ThreadLocalDomainEventIdHolder.addEvents(events);
        }
    }
}
