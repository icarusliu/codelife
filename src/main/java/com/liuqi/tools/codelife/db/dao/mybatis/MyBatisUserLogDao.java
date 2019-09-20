package com.liuqi.tools.codelife.db.dao.mybatis;

import com.liuqi.tools.codelife.db.dao.UserLogDao;
import com.liuqi.tools.codelife.db.dao.mybatis.mapper.UserLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: LiuQI
 * @Created: 2018/3/31 17:32
 * @Version: V1.0
 **/
@Service
public class MyBatisUserLogDao implements UserLogDao {
    @Autowired
    private UserLogMapper userLogMapper;
    
    @Override
    public void logToDB(String username, String operateTime, String userIp, String message) {
        userLogMapper.logToDB(username, operateTime, userIp, message);
    }
}
