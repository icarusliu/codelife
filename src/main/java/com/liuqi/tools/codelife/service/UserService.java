package com.liuqi.tools.codelife.service;

import com.liuqi.tools.codelife.entity.Role;
import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;

import java.util.Collection;
import java.util.List;

/**
 * 用户服务接口
 *
 * @Author: LiuQI
 * @Created: 2018/3/23 20:05
 * @Version: V1.0
 **/
public interface UserService {
    /**
     * 查找所有用户
     *
     * @return 返回所有用户
     */
    public List<User> findAll();
    
    /**
     * 通过用户名查找用户
     * @param username 待查找的用户名
     * @return 未找到时返回空
     * @throws RestException
     */
    public User findByUsername(String username) throws RestException;
    
    /**
     * 锁定用户
     * @param user
     */
    void lockUser(User user);
    
    /**
     * 更新密码错误次数
     * 如果出错次数超过配置次数，需要将账户锁定
     *
     * @param user 用户
     * @param errorCount 需要更新的次数
     */
    void updateErrorCount(User user, int errorCount);
    
    /**
     * 用户注册
     *
     * @param username
     * @param password
     * @param realName 显示名称
     * @throws RestException
     */
    void register(String username, String password, String realName) throws RestException;
    
    /**
     * 解锁用户
     *
     * @param id
     * @throws RestException 当编号为空或者用户不存在时或者用户状态不是锁定状态时
     */
    void unlockUser(Integer id) throws RestException;
    
    /**
     * 注销用户
     * 注销后用户无法再登录
     *
     * @param id
     * @throws RestException 当编号为空或者用户不存在时或者用户状态不是锁定状态时
     */
    void unregisterUser(Integer id)throws RestException;
    
    /**
     * 用户注册后审批通过
     *
     * @param id
     * @throws RestException 当编号为空或者用户不存在时或者用户状态不是锁定状态时
     */
    void approveUser(Integer id)throws RestException;
    
    /**
     * 通过编号查找用户
     * @param id
     * @return  返回查找到的用户，不会返回空值
     * @throws RestException 如果未找到用户时抛出异常
     */
    User findById(Integer id) throws RestException;
    
    /**
     * 根据关键字搜索用户
     * 可以是用户名、ID或者是显示名称
     *
     * @param key
     * @return
     */
    Collection<User> search(String key);
    
    /**
     * 为用户授予角色
     *
     * @param user
     * @param role
     */
    void addRole(User user, Role role);
}
