package com.ark.component.ddd.domain.event;

import java.util.List;
public interface DomainEventDao {

    void insert(List<DomainEvent> events);

    void insert(DomainEvent event);

    DomainEvent byId(Long id);

    List<DomainEvent> byIds(List<Long> ids);

    <T extends DomainEvent> T latestEventFor(Long arId, String type, Class<T> eventClass);

    void successPublish(DomainEvent event);

    void failPublish(DomainEvent event);

    void successConsume(DomainEvent event);

    void failConsume(DomainEvent event);

    List<DomainEvent> tobePublishedEvents(Long startId, int limit);
}
