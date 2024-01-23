package com.ark.ddd.infrastructure;

import com.ark.ddd.domain.AggregateRoot;
import com.ark.ddd.domain.event.DomainEvent;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.Collections.emptyList;
import static java.util.List.copyOf;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

//1. 后缀为byXXX的方法，不会做checkTenantShip检查，在没找到资源时将抛出异常
//2. 后缀为byXxxOptional的方法，不会做checkTenantShip检查，在没找到资源时返回empty()
//3. 后缀为byXxxAndCheckTenantShip的方法，会做checkTenantShip检查，在没找到资源时将抛出异常

@SuppressWarnings({"rawtypes", "unchecked"})
public class MongoBaseRepository<T, M, AR extends AggregateRoot> extends ServiceImpl<M extends BaseMapper<T>, T> {
    private final Map<String, Class> classMapper = new HashMap<>();

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    private DomainEventDao domainEventDao;

    public void save(AR it) {
        requireNonNull(it, "AR must not be null.");

        if (!isEmpty(it.getEvents())) {
            saveEvents(it.getEvents());
            it.clearEvents();
        }

        mongoTemplate.save(it);
    }

    public void save(List<AR> ars) {
        if (isEmpty(ars)) {
            return;
        }

        checkSameTenant(ars);
        List<DomainEvent> events = new ArrayList<>();
        ars.forEach(ar -> {
            if (!isEmpty(ar.getEvents())) {
                events.addAll(ar.getEvents());
                ar.clearEvents();
            }
            mongoTemplate.save(ar);
        });

        saveEvents(events);
    }

    public void insert(AR it) {
        requireNonNull(it, "AR must not be null.");

        if (!isEmpty(it.getEvents())) {
            saveEvents(it.getEvents());
            it.clearEvents();
        }

        mongoTemplate.insert(it);
    }

    public void insert(List<AR> ars) {
        if (isEmpty(ars)) {
            return;
        }

        checkSameTenant(ars);
        List<DomainEvent> events = new ArrayList<>();
        ars.forEach(ar -> {
            if (!isEmpty(ar.getEvents())) {
                events.addAll(ar.getEvents());
                ar.clearEvents();
            }
        });

        mongoTemplate.insertAll(ars);
        saveEvents(events);
    }

    public void delete(AR it) {
        requireNonNull(it, "AR must not be null.");

        if (!isEmpty(it.getEvents())) {
            saveEvents(it.getEvents());
            it.clearEvents();
        }
        mongoTemplate.remove(it);
    }

    public void delete(List<AR> ars) {
        if (isEmpty(ars)) {
            return;
        }
        checkSameTenant(ars);

        List<DomainEvent> events = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        ars.forEach(ar -> {
            if (!isEmpty(ar.getEvents())) {
                events.addAll(ar.getEvents());
                ar.clearEvents();
            }
            ids.add(ar.getId());
        });

        saveEvents(events);
        mongoTemplate.remove(query(where("_id").in(ids)), getType());
    }

    public AR byId(String id) {
        requireNonBlank(id, "AR ID must not be blank.");

        Object it = mongoTemplate.findById(id, getType());
        if (it == null) {
            throw new MryException(AR_NOT_FOUND, "未找到资源。",
                    mapOf("type", getType().getSimpleName(), "id", id));
        }

        return (AR) it;
    }

    public Optional<AR> byIdOptional(String id) {
        requireNonBlank(id, "AR ID must not be blank.");

        Object it = mongoTemplate.findById(id, getType());
        return it == null ? empty() : Optional.of((AR) it);
    }

    public AR byIdAndCheckTenantShip(String id, User user) {
        requireNonBlank(id, "AR ID must not be blank.");
        requireNonNull(user, "User must not be null.");

        AR ar = byId(id);
        checkTenantShip(ar, user);
        return ar;
    }

    public List<AR> byIds(Set<String> ids) {
        if (isEmpty(ids)) {
            return emptyList();
        }

        List<AR> ars = mongoTemplate.find(query(where("_id").in(ids)), getType());
        checkSameTenant(ars);
        return copyOf(ars);
    }

    public List<AR> byIdsAndCheckTenantShip(Set<String> ids, User user) {
        requireNonNull(user, "User must not be null.");

        if (isEmpty(ids)) {
            return emptyList();
        }

        List<AR> ars = byIds(ids);
        ars.forEach(ar -> checkTenantShip(ar, user));
        return copyOf(ars);
    }

    public List<AR> byIdsAll(Set<String> ids) {
        if (isEmpty(ids)) {
            return emptyList();
        }

        List<AR> ars = byIds(ids);
        if (ars.size() != ids.size()) {
            Set<String> fetchedIds = ars.stream().map(AggregateRoot::getId).collect(toImmutableSet());
            Set<String> originalIds = new HashSet<>(ids);
            originalIds.removeAll(fetchedIds);
            throw new MryException(AR_NOT_FOUND_ALL, "未找到所有资源。",
                    mapOf("type", getType().getSimpleName(), "missingIds", originalIds));
        }
        return copyOf(ars);
    }

    public List<AR> byIdsAllAndCheckTenantShip(Set<String> ids, User user) {
        requireNonNull(user, "User must not be null.");

        if (isEmpty(ids)) {
            return emptyList();
        }

        List<AR> ars = byIdsAll(ids);
        ars.forEach(ar -> checkTenantShip(ar, user));
        return copyOf(ars);
    }

    public int count(String tenantId) {
        requireNonBlank(tenantId, "Tenant ID must not be blank.");

        Query query = query(where("tenantId").is(tenantId));
        return (int) mongoTemplate.count(query, getType());
    }

    public boolean exists(String arId) {
        requireNonBlank(arId, "AR ID must not be blank.");
        Query query = query(where("_id").is(arId));
        return mongoTemplate.exists(query, getType());
    }

    private Class getType() {
        String className = getClass().getSimpleName();

        if (!classMapper.containsKey(className)) {
            Type genericSuperclass = getClass().getGenericSuperclass();
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            classMapper.put(className, (Class) actualTypeArguments[0]);
        }

        return classMapper.get(className);
    }

    protected final void checkTenantShip(AggregateRoot ar, User user) {
        requireNonNull(ar, "AR must not be null.");
        requireNonNull(user, "User must not be null.");

        if (!Objects.equals(ar.getTenantId(), user.getTenantId())) {
            throw new MryException(AR_NOT_FOUND, "未找到资源。", mapOf("id", ar.getId(), "tenantId", ar.getTenantId()));
        }
    }

    private void saveEvents(List<DomainEvent> events) {
        if (!isEmpty(events)) {
            domainEventDao.insert(events);
            ThreadLocalDomainEventIdHolder.addEvents(events);
        }
    }

    private void checkSameTenant(Collection<AR> ars) {
        Set<String> tenantIds = ars.stream().map(AR::getTenantId).collect(toImmutableSet());
        if (tenantIds.size() > 1) {
            Set<String> allArIds = ars.stream().map(AggregateRoot::getId).collect(toImmutableSet());
            throw new MryException(SYSTEM_ERROR, "All ars should belong to the same tenant.", "arIds", allArIds);
        }
    }
}
