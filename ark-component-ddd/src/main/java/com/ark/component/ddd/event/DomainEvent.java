//package com.ark.component.ddd.event;
//
//import com.ark.component.ddd.AggregateRoot;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import java.time.Instant;
//import static java.time.Instant.now;
//import static java.util.Objects.requireNonNull;
//import static lombok.AccessLevel.PROTECTED;
//import static org.apache.commons.lang3.StringUtils.isNotBlank;
//
//
////DomainEvent既要保证能支持MongoDB的序列化/反序列化，有要能够通过Jackson序列化/反序列化（因为要发送到Redis）
//@Getter
//@NoArgsConstructor(access = PROTECTED)
//public abstract class DomainEvent {
//    private String id;//事件ID，不能为空
//    private String arTenantId;//事件对应的租户ID，不能为空
//    private String arId;//事件对应的聚合根ID，不能为空
//    private DomainEventType type;//事件类型
//    private DomainEventStatus status;//状态
//    private int publishedCount;//已经发布的次数，无论成功与否
//    private int consumedCount;//已经被消费的次数，无论成功与否
//    private String raisedBy;//引发该事件的memberId
//    private Instant raisedAt;//事件产生时间
//
//    protected DomainEvent(DomainEventType type, User user) {
//        requireNonNull(type, "Domain event type must not be null.");
//        requireNonNull(user, "User must not be null.");
//
//        this.id = newEventId();
//        this.type = type;
//        this.status = DomainEventStatus.CREATED;
//        this.publishedCount = 0;
//        this.consumedCount = 0;
//        this.raisedBy = user.getMemberId();
//        this.raisedAt = now();
//    }
//
//    public String newEventId() {
//        return "EVT" + newSnowflakeId();
//    }
//
//    public void setArInfo(AggregateRoot ar) {
//        this.arTenantId = ar.getTenantId();
//        this.arId = ar.getId();
//    }
//
//    public boolean isConsumedBefore() {
//        return this.consumedCount > 0;
//    }
//
//    public boolean isNotConsumedBefore() {
//        return !isConsumedBefore();
//    }
//
//    public boolean isRaisedByHuman() {
//        return isNotBlank(raisedBy);
//    }
//
//}
