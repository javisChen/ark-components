package com.ark.ddd.domain.event;

import java.util.List;
public interface DomainEventDao {

    void insert(List<DomainEvent> events);

    DomainEvent byId(Long id);

    List<DomainEvent> byIds(List<Long> ids);

    <T extends DomainEvent> T latestEventFor(Long arId, DomainEventType type, Class<T> eventClass);

    void successPublish(DomainEvent event);

    void failPublish(DomainEvent event);

    void successConsume(DomainEvent event);

    void failConsume(DomainEvent event);

    List<DomainEvent> tobePublishedEvents(Long startId, int limit);
}
