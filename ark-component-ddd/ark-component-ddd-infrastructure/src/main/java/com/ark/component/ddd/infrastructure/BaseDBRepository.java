package com.ark.component.ddd.infrastructure;

import com.ark.component.ddd.domain.AggregateRoot;
import com.ark.component.ddd.domain.event.DomainEvent;
import com.ark.component.ddd.domain.event.DomainEventDao;
import com.ark.component.ddd.domain.repository.BaseRepository;
import com.ark.component.ddd.infrastructure.event.ThreadLocalDomainEventIdHolder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cn.hutool.core.lang.Assert.notNull;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;


/**
 * 数据库实现资源库的抽象
 * @param <AR>
 */
public abstract class BaseDBRepository<AR extends AggregateRoot, ID extends Serializable> implements BaseRepository<AR, ID> {

    @Autowired
    private DomainEventDao domainEventDao;

    @Override
    public void saveAndPublishEvents(AR ar) {
        requireNonNull(ar, "AR must not be null.");

        if (!isEmpty(ar.getEvents())) {
            saveEvents(ar.getEvents());
            ar.clearEvents();
        }

        save(ar);
    }

    @Override
    public void saveAndPublishEvents(List<AR> ars) {
        if (isEmpty(ars)) {
            return;
        }

        List<DomainEvent> events = new ArrayList<>();
        ars.forEach(ar -> {
            if (!isEmpty(ar.getEvents())) {
                events.addAll(ar.getEvents());
                ar.clearEvents();
            }
            save(ar);
        });

        saveEvents(events);
    }

    @Override
    public void deleteAndPublishEvents(AR it) {
        requireNonNull(it, "AR must not be null.");

        if (!isEmpty(it.getEvents())) {
            saveEvents(it.getEvents());
            it.clearEvents();
        }
        delete(it);
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

        delete(ars.stream().map(AR::getId).toList());

    }

    @Override
    public AR byIdOrThrowError(ID id) {
        AR ar = byId(id);
        notNull(ar, "Cannot find the ar.");
        return ar;
    }

    private void saveEvents(List<DomainEvent> events) {
        if (!isEmpty(events)) {
            domainEventDao.insert(events);
            ThreadLocalDomainEventIdHolder.addEvents(events);
        }
    }

    protected abstract void save(AR ar);

    protected abstract void delete(AR it);

    protected abstract void delete(List<Long> ids);

}
