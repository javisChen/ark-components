package com.ark.component.ddd.infrastructure.event.db;

import com.ark.component.ddd.domain.AggregateRoot;
import com.ark.component.ddd.domain.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static cn.hutool.core.lang.Assert.notNull;

@Slf4j
@Component
public class MySQLDomainEventDao <T, M, AR extends AggregateRoot> implements DomainEventDao {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MySQLDomainEventDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public void insert(DomainEvent event) {
        insert(List.of(event));
    }

    @Override
    public void insert(List<DomainEvent> events) {
        String sql = """
                insert into domain_event (id, tenant_id, ar_id, type, status, published_count, consumed_count, event_data, triggered_by, triggered_at)\s
                values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        List<Object[]> batchArgs = new ArrayList<>(events.size());
        for (DomainEvent event : events) {
            batchArgs.add(new Object[]{
                    event.getId(),
                    event.getTenantId(),
                    event.getArId(),
                    event.getType(),
                    event.getStatus().name(),
                    event.getPublishedCount(),
                    event.getConsumedCount(),
                    event.getEventData(),
                    event.getTriggeredBy(),
                    event.getTriggeredAt()
            });
        }
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Override
    public DomainEvent byId(Long id) {
        notNull(id, "Domain event ID must not be blank.");
        String sql = "select * from domain_event where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new DomainEventRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new DomainEventException("未找到领域事件");
        }

    }

    @Override
    public List<DomainEvent> byIds(List<Long> ids) {
        notNull(ids, "Domain event IDs must not be null.");
        String sql = "select * from domain_event where id in (:ids) order by triggered_at";
        try {
            return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource("ids", ids), new DomainEventRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new DomainEventException("未找到领域事件");
        }
    }

    @Override
    public <T extends DomainEvent> T latestEventFor(Long arId, String type, Class<T> eventClass) {
        notNull(arId, "AR ID must not be blank.");
        notNull(type, "Domain event type must not be null.");
        notNull(eventClass, "Domain event class must not be null.");

        String sql = "select * from domain_event where arId = ? and type = ? order by triggered_at DESC";

        try {
            return (T) jdbcTemplate.queryForList(sql, new DomainEventRowMapper(), arId, type);
        } catch (EmptyResultDataAccessException e) {
            throw new DomainEventException("未找到领域事件");
        }
    }

    @Override
    public void successPublish(DomainEvent event) {
        notNull(event, "Domain event must not be null.");

        String sql = "update domain_event set status = :status, publishedCount = publishedCount + 1 where id = :id";
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("status", DomainEventStatus.PUBLISH_SUCCEED.name());
        params.put("id", event.getId());
        int updated = namedParameterJdbcTemplate.update(sql, params);
        if (updated < 1) {
            throw new DomainEventException("领域事件发布状态更新失败");
        }

    }

    @Override
    public void failPublish(DomainEvent event) {

        notNull(event, "Domain event must not be null.");

        String sql = "update domain_event set status = :status, publishedCount = publishedCount + 1 where id = :id";
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("status", DomainEventStatus.PUBLISH_FAILED.name());
        params.put("id", event.getId());
        int updated = namedParameterJdbcTemplate.update(sql, params);
        if (updated < 1) {
            throw new DomainEventException("领域事件状态更新失败");
        }

    }

    @Override
    public void successConsume(DomainEvent event) {
        notNull(event, "Domain event must not be null.");

        String sql = "update domain_event set status = :status, consumedCount = consumedCount + 1 where id = :id";
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("status", DomainEventStatus.CONSUME_SUCCEED.name());
        params.put("id", event.getId());
        int updated = namedParameterJdbcTemplate.update(sql, params);
        if (updated < 1) {
            throw new DomainEventException("领域事件状态更新失败");
        }

    }

    @Override
    public void failConsume(DomainEvent event) {

        String sql = "update domain_event set status = :status, consumedCount = consumedCount + 1 where id = :id";
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("status", DomainEventStatus.CONSUME_FAILED.name());
        params.put("id", event.getId());
        int updated = namedParameterJdbcTemplate.update(sql, params);
        if (updated < 1) {
            throw new DomainEventException("领域事件状态更新失败");
        }

    }

    @Override
    public List<DomainEvent> tobePublishedEvents(Long startId, int limit) {
        notNull(startId, "Start ID must not be blank.");

        String sql = """
                select * from domain_event where id > (:startId)
                 and publishedCount < 3
                 and consumedCount < 3
                 and status in('CREATED', 'PUBLISH_FAILED', 'CONSUME_FAILED')
                 order by triggered_at
                """;
        try {
            HashMap<String, Object> params = new HashMap<>(2);
            params.put("startId", startId);
            params.put("limit", limit);
            return namedParameterJdbcTemplate.query(sql, params, new DomainEventRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new DomainEventException("未找到领域事件");
        }

    }

    public static class DomainEventRowMapper implements RowMapper<DomainEvent> {
        @Override
        public DomainEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
            DomainEvent event = new DomainEvent(null, rs.getString("type"));
            event.setId(rs.getLong("id"));
            event.setTenantId(rs.getLong("tenantId"));
            event.setArId(rs.getLong("arId"));
            event.setStatus(DomainEventStatus.valueOf(rs.getString("status")));
            event.setPublishedCount(rs.getInt("publishedCount"));
            event.setConsumedCount(rs.getInt("consumedCount"));
            event.setTriggeredBy(rs.getLong("triggeredBy"));
            event.setTriggeredAt(rs.getDate("triggeredBy").toInstant());
            return event;
        }
    }
}
