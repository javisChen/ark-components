package com.ark.ddd.domain;

import cn.hutool.core.util.IdUtil;
import com.ark.ddd.domain.event.DomainEvent;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

@Getter
public abstract class AggregateRoot implements Identity {
    private static final int MAX_OPS_LOG_SIZE = 20;

    private Long id;//通过Snowflake算法生成
    private Long tenantId = 1L;//在多租户下，所有聚合根都需要一个tenantId来对应某个租户
    private Instant createdAt;//创建时间
    private Long creator;//创建人Id
    private Instant updatedAt;//更新时间
    private Long modifier;//更新人MemberId
    private List<DomainEvent> events;//领域事件列表，用于临时存放完成某个业务流程中所发出的事件，会被BaseRepository保存到事件表中
    // private LinkedList<OpsLog> opsLogs;//操作日志

//    protected AggregateRoot() {
//        this.clearEvents();
//    }

    public AggregateRoot() {
//        notNull(user, "User must not be null.");
//        notBlank(user.getTenantId(), "Tenant ID must not be blank.");

        this.id = IdUtil.getSnowflakeNextId();
//        this.tenantId = 1L;
        this.createdAt = now();
//        this.creator = user.getMemberId();
//        this.creator = user.getName();
    }

//    protected AggregateRoot(Long id, Long tenantId) {
//        notNull(id, "AR ID must not be blank.");
//        notNull(tenantId, "Tenant ID must not be blank.");
////        notNull(user, "User must not be null.");
//
//        this.id = id;
//        this.tenantId = tenantId;
//        this.createdAt = now();
////        this.creator = user.getMemberId();
////        this.creator = user.getName();
//    }

//    protected void addOpsLog(String note, User user) {
//        if (user.isLoggedIn()) {
//            OpsLog log = OpsLog.builder().note(note).optAt(now()).optBy(user.getMemberId()).obn(user.getName()).build();
//            List<OpsLog> opsLogs = allOpsLogs();
//
//            opsLogs.add(log);
//            if (opsLogs.size() > MAX_OPS_LOG_SIZE) {//最多保留最近100条
//                this.opsLogs.remove();
//            }
//
//            this.updatedAt = now();
//            this.modifier = user.getMemberId();
//            this.updater = user.getName();
//        }
//    }
//
//    private List<OpsLog> allOpsLogs() {
//        if (opsLogs == null) {
//            this.opsLogs = new LinkedList<>();
//        }
//
//        return opsLogs;
//    }

    protected void publishEvent(DomainEvent event) {
        event.setArInfo(this);
        allEvents().add(event);
    }

    public void clearEvents() {
        this.events = null;
    }

    private List<DomainEvent> allEvents() {
        if (events == null) {
            this.events = new ArrayList<>();
        }

        return events;
    }

    @Override
    public Long getIdentifier() {
        return id;
    }

}
