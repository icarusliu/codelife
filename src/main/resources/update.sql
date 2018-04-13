-- 1.3 增加评价模块
create table comment(
    id integer not null auto_increment comment '编号',
    user_id integer comment '评论用户编号',
    ip varchar(16) comment '用户IP',
    content varchar(1000) comment '评论内容',
    comment_time varchar(20) comment '评论时间',
    anonymos tinyint default 0 comment '是否匿名，0：不匿名，1：匿名',
    type tinyint default 0 comment '评论类型，0：文章；1：专题；2: 评论',
    destination integer not null comment '评论对象，如某个专题或者某个文章或者某个评论',
    primary key (id)
);