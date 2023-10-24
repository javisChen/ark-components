create table if not exists `trade`.stm_statemachine_definition
(
    id           bigint unsigned auto_increment
    primary key,
    biz_code     varchar(50)      default ''                not null comment '业务编码',
    config       varchar(1024)    default ''                not null comment '状态机规则配置（JSON）',
    status       tinyint unsigned default 1                 not null comment '启用状态 0-禁用；1-启用；',
    gmt_modified datetime         default CURRENT_TIMESTAMP not null,
    gmt_create   datetime         default CURRENT_TIMESTAMP not null,
    creator      bigint unsigned  default 0                 not null comment '创建人',
    modifier     bigint unsigned  default 0                 not null comment '更新人',
    is_deleted   bigint unsigned  default 0                 not null comment '删除标识 0-表示未删除 大于0-已删除',
    constraint uk_biz_code
    unique (biz_code)
    )
    comment '状态机规则定义表' auto_increment = 2;

create table if not exists `trade`.stm_history
(
    id            bigint unsigned auto_increment
    primary key,
    biz_code      varchar(50)     default ''                not null comment '业务编码',
    biz_id        bigint                                    not null comment '业务ID',
    event         varchar(64)     default ''                not null comment '驱动的事件',
    pre_state     varchar(64)     default ''                not null comment '转换前状态',
    current_state varchar(64)     default ''                not null comment '当前状态',
    gmt_modified  datetime        default CURRENT_TIMESTAMP not null,
    gmt_create    datetime        default CURRENT_TIMESTAMP not null,
    creator       bigint unsigned default 0                 not null comment '创建人',
    modifier      bigint unsigned default 0                 not null comment '更新人',
    is_deleted    bigint unsigned default 0                 not null comment '删除标识 0-表示未删除 大于0-已删除'
    )
    comment '状态机历史表' auto_increment = 1513128736577089539;

create index idx_biz_code_biz_id
    on `trade`.stm_history (biz_code, biz_id);

create table if not exists `trade`.stm_state
(
    id           bigint unsigned auto_increment
    primary key,
    biz_code     varchar(50)     default ''                not null comment '业务编码',
    biz_id       bigint                                    not null comment '业务ID',
    state        varchar(64)     default ''                not null comment '状态',
    finished     tinyint(1)      default 0                 not null comment '完结状态 0-否 1-是',
    gmt_modified datetime        default CURRENT_TIMESTAMP not null,
    gmt_create   datetime        default CURRENT_TIMESTAMP not null,
    creator      bigint unsigned default 0                 not null comment '创建人',
    modifier     bigint unsigned default 0                 not null comment '更新人',
    is_deleted   bigint unsigned default 0                 not null comment '删除标识 0-表示未删除 大于0-已删除'
    )
    comment '状态机运行时表' auto_increment = 1513128736400928770;

create index idx_biz_code_biz_id
    on `trade`.stm_state (biz_code, biz_id);

