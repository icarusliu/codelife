package com.liuqi.tools.codelife.db.dao;

/**
 * 用户日志Dao
 *
 *
 * @Author: LiuQI
 * @Created: 2018/3/31 17:14
 * @Version: V1.0
 **/
public interface UserLogDao {
    /**
     * 记录用户操作日志到DB中
     *
     * @param username
     * @param operateTime
     * @param userIp
     * @param message
     */
    void logToDB(String username, String operateTime, String userIp, String message);
}
