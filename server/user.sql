-- auto-generated definition
create table sys_user
(
    id            bigint auto_increment comment '主键'
        primary key,
    user_name     varchar(256)                       null comment '用户名',
    user_account  varchar(256)                       not null comment '登录账户',
    user_password varchar(256)                       not null comment '用户密码',
    avatar_url    varchar(256)                       null comment '用户头像地址',
    gender        tinyint  default 0                 not null comment '用户性别',
    phone         varchar(256)                       null comment '用户手机号',
    email         varchar(256)                       null comment '用户邮箱',
    user_role     int      default 1                 null comment '用户角色',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted    tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)',
    user_status   varchar(256)                       null comment '用户状态'
)
    comment '用户表';