create table article_comment(
    id integer not null auto_increment comment '编号',
    user_id integer comment '评论用户编号',
    ip varchar(16) comment '用户IP',
    content varchar(1000) comment '评论内容',
    comment_time varchar(20) comment '评论时间',
    anonymos tinyint default 0 comment '是否匿名，0：不匿名，1：匿名',
    article integer default 0 comment '文章对象',
    parent integer comment '父级评论',
    primary key (id)
);

insert into article_comment
select id, user_id, ip, content, comment_time, anonymos, destination, null from comment where type = 0;

insert into article_comment
select t1.id, t1.user_id, t1.ip, t1.content, t1.comment_time, t1.anonymos, t2.destination, t2.id from comment t1
left join comment t2 on t1.destination = t2.id
where t1.type = 2;

alter table article_comment add column show_name varchar(100) comment '显示名称';