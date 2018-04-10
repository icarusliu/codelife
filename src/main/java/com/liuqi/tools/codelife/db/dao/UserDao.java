package com.liuqi.tools.codelife.db.dao;

import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.entity.UserStatus;

import java.util.Collection;
import java.util.List;

/**
 * 用户操作Dao接口
 *
 * @Author: LiuQI
 * @Created: 2018/3/23 21:59
 * @Version: V1.0
 **/
public interface UserDao {
    /**
     * 查找所有用户
     *
     * @return 返回所有用户清单
     */
    List<User> findAll();
    
    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 返回查找的用户对象，如果未查找到返回Null
     */
    User findByUsername(String username);
    
    /**
     * 通过编号查找用户
     *
     * @param id
     * @return 如果未找到时返回Null
     */
    User findById(Integer id);
    
    /**
     * 修改用户状态
     * @param id 需要修改状态的用户编号
     * @param locked
     */
    void updateUserStatus(int id, UserStatus locked);

    /**
     * 更新用户信息
     *
     * @param user
     */
    void updateUser(User user);
    
    /**
     * 新增用户
     *
     * @param user
     */
    void addUser(User user);
    
    /**
     * 通过关键字搜索用户
     *
     * @param key
     * @return
     */
    Collection<User> search(String key);
}
