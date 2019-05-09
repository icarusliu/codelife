package com.liuqi.tools.codelife.service;

import com.liuqi.tools.codelife.db.entity.User;

/**
 * 授权对象服务类
 * 主要用于从Security的Context中获取登录的用户信息
 *
 * @Author: LiuQI
 * @Created: 2018/3/27 19:13
 * @Version: V1.0
 **/
public interface AuthenticationService {
    /**
     * 获取登录的用户
     *
     * @return 返回获取的登录用户
     */
    User getLoginUser();
    
    /**
     * 获取登录用户的IP
     *
     * @return
     */
    String getLoginUserIp();

    /**
     * 退出登录
     */
    void logout();
}
