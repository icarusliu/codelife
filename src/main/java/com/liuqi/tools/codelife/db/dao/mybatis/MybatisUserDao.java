package com.liuqi.tools.codelife.db.dao.mybatis;

import com.liuqi.tools.codelife.db.dao.mybatis.mapper.UserMapper;
import com.liuqi.tools.codelife.db.dao.UserDao;
import com.liuqi.tools.codelife.entity.Role;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author: LiuQI
 * @Created: 2018/3/23 21:58
 * @Version: V1.0
 **/
@Repository
public class MybatisUserDao implements UserDao{
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }
    
    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
    
    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }
    
    @Override
    public void updateUserStatus(int id, UserStatus locked) {
        userMapper.updateUserStatus(id, locked.ordinal());
    }
    
    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }
    
    /**
     * 新增用户
     *
     * @param user
     */
    @Override
    public void addUser(User user) {
        userMapper.addUser(user);
    }
    
    @Override
    public Collection<User> search(String key) {
        return Optional.ofNullable(userMapper.search(key)).orElse(Collections.EMPTY_LIST);
    }
    
    @Override
    public void addRole(User user, Role role) {
        userMapper.addRole(user.getId(), role.getId());
    }
    
    @Override
    public Collection<User> getTopicUsers(Integer topicId) {
        return userMapper.getTopicUsers(topicId);
    }
    
    
}
