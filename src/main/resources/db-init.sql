--
-- Table structure for table `user`
--
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(100) NOT NULL DEFAULT '888888',
  `realname` varchar(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `error_count` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

INSERT INTO `user` VALUES (1,'admin','admin','管理员',1,0),(2,'test','test','测试用户',1,0),(3,'a','1234','a',0,0),(4,'aa','1234','aa',0,0);
-- Dump completed on 2018-04-01 12:51:28

-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: pcontent
-- ------------------------------------------------------
-- Server version	5.7.21-log

--
-- Table structure for table `user_logs`
--

DROP TABLE IF EXISTS `user_logs`;
CREATE TABLE `user_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operate_time` varchar(20) DEFAULT NULL,
  `message` varchar(200) DEFAULT NULL,
  `user_ip` varchar(16) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL COMMENT '操作用户名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=381 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_logs`
--

-- Dump completed on 2018-04-01 12:51:28


-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: pcontent
-- ------------------------------------------------------
-- Server version	5.7.21-log

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` VALUES (1,1,1),(2,2,2);


-- Dump completed on 2018-04-01 12:51:28


-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: pcontent
-- ------------------------------------------------------
-- Server version	5.7.21-log

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `author` int(11) DEFAULT NULL,
  `create_date` varchar(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `content_url` varchar(100) DEFAULT NULL,
  `remark` varchar(300) DEFAULT NULL,
  `read_count` int(11) DEFAULT NULL,
  `praise_count` mediumint(9) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `article`
--
-- Dump completed on 2018-04-01 12:51:28


-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: pcontent
-- ------------------------------------------------------
-- Server version	5.7.21-log

--
-- Table structure for table `article_type`
--

DROP TABLE IF EXISTS `article_type`;
CREATE TABLE `article_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `article_type`
--

INSERT INTO `article_type` VALUES (1,'WEB开发',1),(2,'编程语言',1),(3,'数据库',1),(4,'前端应用',1);

-- Dump completed on 2018-04-01 12:51:27


-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: pcontent
-- ------------------------------------------------------
-- Server version	5.7.21-log

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `role`
--

INSERT INTO `role` VALUES (1,'ADMIN',NULL),(2,'USER',NULL);

-- Dump completed on 2018-04-01 12:51:27

/*!40101 SET character_set_client = @saved_cs_client */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



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

-- Topic 增加管理员、类型字段
alter table topic add column status tinyint not null default 0 comment '专题状态，0：待审批中；1：正常；';
alter table topic add column type tinyint not null default 0 comment '专题类型，0：开放式；1：封闭式';
alter table topic add column creator int not null default 1 comment '创建者';
alter table topic add column admin int comment '专题管理员';

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