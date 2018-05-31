package com.liuqi.tools.codelife.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: LiuQI
 * @Created: 2018/3/31 17:56
 * @Version: V1.0
 **/
@Mapper
public interface UserLogDao {
    /**
     * 将用户操作日志记录到DB
     *
     * @param username
     * @param operateTime
     * @param userIp
     * @param message
     */
    void logToDB(@Param("username") String username,
                 @Param("operateTime") String operateTime,
                 @Param("userIp") String userIp,
                 @Param("message") String message);
}
