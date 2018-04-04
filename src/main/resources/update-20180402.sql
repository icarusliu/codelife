-- V1.1 增加用户注册时间
-- V1.2 增加专题
alter table user add column register_time varchar(12) comment '注册时间';

-- 创建topic表
create table topic(
    id integer not null auto_increment comment '编号',
    title varchar(100) comment '名称',
    introduction varchar(200) comment '介绍',
    img varchar(100) comment '专题封面图片',
    primary key(id)
);

-- 创建文章Topic关系表
create table topic_article(
    id integer not null auto_increment comment '编号',
    article_id integer not null comment '文章编号',
    topic_id integer not null comment '专题编号',
    primary key(id),
    constraint FK_TOPIC_ARTICLE_TOPIC foreign key (topic_id) references topic(id),
    constraint FK_TOPIC_ARTICLE_ARTICLE foreign key (article_id) references article(id)
);

-- 创建订阅表 用户可订阅某个专题
create table topic_user(
    id integer not null auto_increment comment '编号',
    user_id integer not null comment '用户编号',
    topic_id integer not null comment '专题编号',
    primary key(id),
    constraint FK_TOPIC_USER_TOPIC foreign key (topic_id) references topic(id),
    constraint FK_TOPIC_USER_USER foreign key (user_id) references user(id)
);
