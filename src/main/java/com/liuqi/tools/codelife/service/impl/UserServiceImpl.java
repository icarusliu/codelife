package com.liuqi.tools.codelife.service.impl;

import com.liuqi.tools.codelife.db.dao.UserDao;
import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.entity.UserStatus;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.UserService;
import com.liuqi.tools.codelife.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 用户服务默认实现类
 *
 * @Author: LiuQI
 * @Created: 2018/3/23 22:04
 * @Version: V1.0
 **/
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;
    
    @Value("${app.user.maxErrorCount}")
    private String maxErrorCount;
    
    @Value("${app.restrict.maxUsernameLength}")
    private String maxUsernameLength;
    
    @Value("${app.restrict.maxPasswordLength}")
    private String maxPasswordLength;
    
    @Value("${app.restrict.maxRealNameLength}")
    private String maxRealNameLength;
    
    /**
     * 查找所有用户
     *
     * @return 返回所有用户清单 如果没有用户则返回一个空的ArrayList
     */
    @Override
    public List<User> findAll() {
        List<User> list = userDao.findAll();
        if (null == list) {
            return new ArrayList<>();
        } else {
            return list;
        }
    }
    
    /**
     * 通过用户名查找用户
     * 不会返回Null值
     *
     * @param username 待查找的用户名
     * @throws RestException 当用户名不存在时抛出
     * @return
     */
    @Override
    public User findByUsername(String username) throws RestException {
        User user = userDao.findByUsername(username);
        if (null == user) {
            throw new RestException("用户名不存在！");
        }
        
        return user;
    }
    
    @Override
    public void lockUser(User user) {
        userDao.updateUserStatus(user.getId(), UserStatus.LOCKED);
    }
    
    /**
     * 更新用户出错次数
     * 如果出错超过一定次数需要将用户锁定
     * 如果设置出错次数为0且用户状态是锁定状态则需要将用户解锁
     * 否则单纯的修改出错次数
     *
     * @Param user 用户
     * @Param errorCount 需要更新的次数
     */
    @Override
    public void updateErrorCount(User user, int errorCount) {
        if (errorCount == Integer.valueOf(maxErrorCount)) {
            //需要修改的出错达到最大出错次数，则需要将用户锁定
            //但如果用户状态不是正常状态时不需要处理
            if (!UserStatus.NORMAL.equals(user.getStatus())) {
                logger.warn("User status is not normal, no need to modify its' errorCount to max!");
                return;
            }
            
            user.setStatus(UserStatus.LOCKED);
        } else if (0 == errorCount && UserStatus.LOCKED == user.getStatus()) {
            //修改出错次数成0，并且用户状态是被锁定状态，则需要将其状态修改成正常状态
            user.setStatus(UserStatus.NORMAL);
        }
        
        //单纯的修改出错次数
        user.setErrorCount(errorCount);
        
        userDao.updateUser(user);
    }
    
    /**
     * 用户注册
     *
     * @param username
     * @param password
     * @param realName 显示名称
     */
    @Override
    public void register(String username, String password, String realName) throws RestException {
        Assert.notNull(username, "Username cannot be null!");
        Assert.notNull(password, "Password cannot be null!");
        Assert.notNull(realName, "realName cannot be null!");
        
        //为空检测
        username = username.trim();
        password = password.trim();
        realName = realName.trim();
        
        if ("".equals(username)) {
            logger.error("Username cannot be empty!");
            throw new RestException("用户名不能为空！");
        } else if ("".equals(password)) {
            logger.error("Password cannot be empty!");
            throw new RestException("密码不能为空！");
        }
        
        //检测用户名是否存在
        try {
            findByUsername(username);
    
            logger.error("User with the name of {} already exists!", username);
            throw new RestException("用户名重复！");
        } catch (RestException ex) {
        
        }
        
        //检测用户名长度、密码长度以及实际长度是否超过限制
        if (Integer.valueOf(maxUsernameLength) < username.length()) {
            logger.error("Username too long, username: {}, max allowed length: {}!", username, maxUsernameLength);
            throw new RestException("用户名过长");
        } else if (Integer.valueOf(maxPasswordLength) < password.length()) {
            logger.error("Username too long, username: {}, max allowed length: {}!", password, maxPasswordLength);
            throw new RestException("密码过长");
        } else if (Integer.valueOf(maxRealNameLength) < realName.length()) {
            logger.error("RealName too long, username: {}, max allowed length: {}!", realName, maxRealNameLength);
            throw new RestException("显示名称过长");
        }
        
        //添加用户
        User user = new User();
        user.setErrorCount(0);
        user.setStatus(UserStatus.APPROVING);
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(realName);
        user.setRegisterTime(DateUtils.getNowDateStr());
        
        userDao.addUser(user);
    }
    
    /**
     * 解锁用户
     * 用户需要在锁定状态才能解锁（如果是注销状态也可以通过解锁操作来使得它变成正常！）
     *
     * @param id
     * @throws RestException 当编号为空或者用户不存在时或者用户状态不是锁定状态或者不是注销状态时
     */
    @Override
    public void unlockUser(Integer id) throws RestException {
        User user = findById(id);
        if (!UserStatus.LOCKED.equals(user.getStatus()) && !UserStatus.CANCEL.equals(user.getStatus())) {
            logger.error("User status is not locked, cannot be unlocked!");
            throw new RestException("用户状态不是锁定状态，不能进行解锁！");
        }
        
        userDao.updateUserStatus(id, UserStatus.NORMAL);
    }
    
    @Override
    public void unregisterUser(Integer id) throws RestException {
        //ADMIN不能被注销
        User user = findById(id);
        if (user.getUsername().equals("admin")) {
            logger.error("Admin cannot be unregistered!");
            throw new RestException("不能注销管理员！");
        }
        
        userDao.updateUserStatus(id, UserStatus.CANCEL);
    }
    
    @Override
    public void approveUser(Integer id) throws RestException {
        User user = findById(id);
        if (!UserStatus.APPROVING.equals(user.getStatus())) {
            logger.error("User status is not approving, cannot be approved!");
            throw new RestException("用户状态不是待审批状态，不能被审批！");
        }
        
        userDao.updateUserStatus(id, UserStatus.NORMAL);
    }
    
    @Override
    public User findById(Integer id) throws RestException {
        if (null == id) {
            logger.error("ID cannot be null!");
            throw new RestException("编号不能为空！");
        }
        
        User user = userDao.findById(id);
        if (null == user) {
            logger.error("User with the special id does not exist, id: {}!", id);
            throw new RestException("用户不存在！");
        }
        
        return user;
    }
    
    /**
     * 通过关键字搜索用户
     *
     * @param key
     * @return
     */
    @Override
    public Collection<User> search(String key) {
        key = key.trim();
        if ("".equals(key)) {
            return Collections.EMPTY_LIST;
        }
        
        return userDao.search(key);
    }
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
}
