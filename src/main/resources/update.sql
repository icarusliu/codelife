-- 增加文章置顶功能
alter table article add column fix_top tinyint default 0 comment '是否置顶：0：不置顶，1：置顶';
