package com.ark.component.ddd.infrastructure;

import com.ark.component.ddd.domain.AggregateRoot;
import com.ark.component.ddd.domain.event.DomainEvent;
import com.ark.component.ddd.domain.repository.BaseRepository;
import com.ark.component.ddd.infrastructure.event.ThreadLocalDomainEventIdHolder;
import com.ark.component.exception.ExceptionFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static cn.hutool.core.lang.Assert.notNull;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;


/**
 * 数据库实现资源库的抽象
 * @param <AR>
 */
public abstract class BaseDBRepository<AR extends AggregateRoot, ID extends Serializable> implements BaseRepository<AR, ID> {

//    @Autowired
//    private DomainEventDao domainEventDao;

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
    public void deleteAndPublishEvents(List<AR> ars) {
        if (isEmpty(ars)) {
            return;
        }
        List<DomainEvent> events = new ArrayList<>();
        List<Long> ids = new ArrayList<>(ars.size());
        ars.forEach(ar -> {
            if (!isEmpty(ar.getEvents())) {
                events.addAll(ar.getEvents());
                ar.clearEvents();
            }
            ids.add(ar.getId());
        });

        saveEvents(events);

        delete(ids);
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
        return byIdOrThrowError(id, ExceptionFactory.userExceptionSupplier("资源不存在"));
    }

    @Override
    public AR byIdOrThrowError(ID id, String msg) {
        return byIdOrThrowError(id, ExceptionFactory.userExceptionSupplier(msg));
    }

    @Override
    public <X extends Throwable> AR byIdOrThrowError(ID id, Supplier<X> throwable) throws X {
        AR ar = byId(id);
        notNull(ar, throwable);
        return ar;
    }

    private void saveEvents(List<DomainEvent> events) {
        if (!isEmpty(events)) {
            // domainEventDao.insert(events);
            ThreadLocalDomainEventIdHolder.addEvents(events);
        }
    }

    public abstract void save(AR ar);

    public abstract void delete(AR ar);

    public void delete(List<Long> ids) {

    }

}
