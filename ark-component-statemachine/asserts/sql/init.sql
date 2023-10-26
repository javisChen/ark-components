create table if not exists `trade`.stm_history
(
    id            bigint unsigned auto_increment
        primary key,
    machine_id    varchar(64)     default ''                not null comment '状态机id',
    biz_id        varchar(64)                               not null comment '业务id',
    event         varchar(64)     default ''                not null comment '驱动的事件',
    pre_state     varchar(64)     default ''                not null comment '转换前状态',
    current_state varchar(64)     default ''                not null comment '当前状态',
    extras        varchar(2048)                             null comment '状态流转的参数',
    remark        varchar(2048)   default ''                not null comment '保留信息',
    gmt_create    datetime        default CURRENT_TIMESTAMP not null,
    creator       bigint unsigned default 0                 not null comment '创建人',
    modifier      bigint unsigned default 0                 not null comment '更新人'
)
    comment '状态机历史表' auto_increment = 1513128736577089539;

create index idx_machine_id_biz_id
    on `trade`.stm_history (machine_id, biz_id);

create table if not exists `trade`.stm_state
(
    id           bigint unsigned auto_increment
        primary key,
    machine_id   varchar(64)     default ''                not null comment '状态机id',
    biz_id       varchar(64)                               not null comment '业务id',
    state        varchar(64)     default ''                not null comment '状态',
    ended        tinyint(1)      default 0                 not null comment '完结状态 0-否 1-是',
    gmt_modified datetime        default CURRENT_TIMESTAMP not null,
    gmt_create   datetime        default CURRENT_TIMESTAMP not null,
    creator      bigint unsigned default 0                 not null comment '创建人',
    modifier     bigint unsigned default 0                 not null comment '更新人'
)
    comment '状态机数据记录表';

create unique index idx_machine_id_biz_id
    on `trade`.stm_state (machine_id, biz_id);

