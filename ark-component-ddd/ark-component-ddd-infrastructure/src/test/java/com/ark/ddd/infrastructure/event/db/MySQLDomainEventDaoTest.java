package com.ark.ddd.infrastructure.event.db;

import cn.hutool.core.util.IdUtil;
import com.ark.component.ddd.domain.event.DomainEvent;
import com.ark.component.ddd.domain.event.DomainEventStatus;
import com.ark.component.ddd.infrastructure.event.db.MySQLDomainEventDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class MySQLDomainEventDaoTest {

    private MySQLDomainEventDao dao;

    @BeforeEach
    public void before() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder.create()
                .username("root")
                .password("root")
                .url("jdbc:mysql://localhost:33060/iam")
                .build());
        dao = new MySQLDomainEventDao(jdbcTemplate);
    }

    @Test
    @Transactional
    void should_insert_success() {
        List<DomainEvent> events = new ArrayList<>();
        User user = new User("JC");
        DomainEvent domainEvent = new UserCreatedEvent(user.getId());

        domainEvent.setArInfo(user);
        domainEvent.setId(IdUtil.getSnowflakeNextId());
        domainEvent.setType("USER_CREATED");
        domainEvent.setStatus(DomainEventStatus.CREATED);
        domainEvent.setPublishedCount(0);
        domainEvent.setConsumedCount(0);
        domainEvent.setTriggeredBy(1L);
        domainEvent.setTriggeredAt(Instant.now());
        domainEvent.setEventData("");
        events.add(domainEvent);

        dao.insert(events);
    }

    @Test
    void byId() {
    }

    @Test
    void byIds() {
    }

    @Test
    void latestEventFor() {
    }

    @Test
    void successPublish() {
    }

    @Test
    void failPublish() {
    }

    @Test
    void successConsume() {
    }

    @Test
    void failConsume() {
    }

    @Test
    void tobePublishedEvents() {
    }
}