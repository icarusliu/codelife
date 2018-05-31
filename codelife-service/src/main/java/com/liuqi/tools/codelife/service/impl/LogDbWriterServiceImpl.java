package com.liuqi.tools.codelife.service.impl;

import com.liuqi.tools.codelife.db.dao.UserLogDao;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.LogDbWriterService;
import com.liuqi.tools.codelife.tool.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

/**
 * 向DB记录日志的服务实现类
 * 为减轻对请求处理时间的影响，使用后台异步处理的方式来记录日志。
 *
 * @Author: LiuQI
 * @Created: 2018/3/31 17:11
 * @Version: V1.0
 **/
@Service
public class LogDbWriterServiceImpl implements LogDbWriterService {
    @Autowired
    private TaskExecutor taskExecutor;
    
    @Autowired
    private UserLogDao userLogDao;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Override
    public void log(String logMessage) {
        User loginUser = authenticationService.getLoginUser();
        String operateTime = DateUtils.getNowDateStr();
        String userIp = authenticationService.getLoginUserIp();
        
        String username = (null != loginUser) ? loginUser.getUsername() : "未登录用户";
        taskExecutor.execute(() -> userLogDao.logToDB(username, operateTime, userIp, logMessage));
    }
}
