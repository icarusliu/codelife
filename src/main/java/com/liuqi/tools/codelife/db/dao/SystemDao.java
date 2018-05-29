package com.liuqi.tools.codelife.db.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * 系统数据库操作类
 * 提供如数据库表是否已创建等功能
 *
 * @author LiuQI 2018/5/29 13:19
 * @version V1.0
 **/
@Mapper
public interface SystemDao {
    /**
     * 数据库是否已初始化
     *
     * @return 0：未初始化；1：已初始化
     */
    int dbInited();
}
