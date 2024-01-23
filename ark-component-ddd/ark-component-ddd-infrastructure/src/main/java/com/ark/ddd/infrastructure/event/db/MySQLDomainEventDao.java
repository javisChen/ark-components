package com.ark.ddd.infrastructure.event.db;

import com.ark.ddd.domain.event.*;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static cn.hutool.core.lang.Assert.notBlank;
import static cn.hutool.core.lang.Assert.notNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class MySQLDomainEventDao implements DomainEventDao {
    private final JdbcClient jdbcClient;

    @Override
    public void insert(List<DomainEvent> events) {
        jdbcClient.sql().
                update();
        notNull(events, "Domain events must not be null.");

        mongoTemplate.insertAll(events);
    }

    @Override
    public DomainEvent byId(String id) {
        notBlank(id, "Domain event ID must not be blank.");

        DomainEvent domainEvent = null;
        try {
            domainEvent = jdbcClient
                    .sql("select * from domain_event where id = ?")
                    .param(id)
                    .query((rs, rowNum) -> {
                        DomainEvent domainEvent1 = new DomainEvent();
                        domainEvent1.setId(rs.getLong("id"));
                        domainEvent1.setTenantId(rs.getLong("tenantId"));
                        domainEvent1.setArId(rs.getLong("arId"));
                        domainEvent1.setType(rs.getString("type"));
                        domainEvent1.setStatus(DomainEventStatus.valueOf(rs.getString("status")));
                        domainEvent1.setPublishedCount(rs.getInt("publishedCount"));
                        domainEvent1.setConsumedCount(rs.getInt("consumedCount"));
                        domainEvent1.setTriggeredBy(rs.getLong("triggeredBy"));
                        domainEvent1.setTriggeredAt(rs.getDate("triggeredBy").toInstant());
                        return domainEvent1;
                    }).single();
        } catch (EmptyResultDataAccessException e) {
            throw new DomainEventException("未找到领域事件");
        }

        return domainEvent;
    }

    @Override
    public List<DomainEvent> byIds(List<String> ids) {
        requireNonNull(ids, "Domain event IDs must not be null.");

        return mongoTemplate.find(query(where("_id").in(ids)).with(by(ASC, "raisedAt")), DomainEvent.class);
    }

    @Override
    public <T extends DomainEvent> T latestEventFor(String arId, DomainEventType type, Class<T> eventClass) {
        requireNonBlank(arId, "AR ID must not be blank.");
        requireNonNull(type, "Domain event type must not be null.");
        requireNonNull(eventClass, "Domain event class must not be null.");

        Query query = query(where("arId").is(arId).and("type").is(type)).with(by(DESC, "raisedAt"));
        return mongoTemplate.findOne(query, eventClass);
    }

    @Override
    public void successPublish(DomainEvent event) {
        requireNonNull(event, "Domain event must not be null.");

        Query query = Query.query(where("_id").is(event.getId()));
        Update update = new Update();
        update.set("status", PUBLISH_SUCCEED.name()).inc("publishedCount");
        mongoTemplate.updateFirst(query, update, DomainEvent.class);
    }

    @Override
    public void failPublish(DomainEvent event) {
        requireNonNull(event, "Domain event must not be null.");

        Query query = Query.query(where("_id").is(event.getId()));
        Update update = new Update();
        update.set("status", PUBLISH_FAILED.name()).inc("publishedCount");
        mongoTemplate.updateFirst(query, update, DomainEvent.class);
    }

    @Override
    public void successConsume(DomainEvent event) {
        requireNonNull(event, "Domain event must not be null.");

        Query query = Query.query(where("_id").is(event.getId()));
        Update update = new Update();
        update.set("status", CONSUME_SUCCEED.name()).inc("consumedCount");
        mongoTemplate.updateFirst(query, update, DomainEvent.class);
    }

    @Override
    public void failConsume(DomainEvent event) {
        requireNonNull(event, "Domain event must not be null.");

        Query query = Query.query(where("_id").is(event.getId()));
        Update update = new Update();
        update.set("status", CONSUME_FAILED.name()).inc("consumedCount");
        mongoTemplate.updateFirst(query, update, DomainEvent.class);
    }

    @Override
    public List<DomainEvent> tobePublishedEvents(String startId, int limit) {
        requireNonBlank(startId, "Start ID must not be blank.");

        Query query = query(where("status").in(CREATED, PUBLISH_FAILED, CONSUME_FAILED)
                .and("_id").gt(startId)
                .and("publishedCount").lt(3)
                .and("consumedCount").lt(3))
                .with(by(ASC, "raisedAt"))
                .limit(limit);
        return mongoTemplate.find(query, DomainEvent.class);
    }
}
