package com.liuqi.tools.codelife.db.dao.mybatis.mapper;

import com.liuqi.tools.codelife.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户Mapper
 *
 * @Author: LiuQI
 * @Created: 2018/3/23 21:01
 * @Version: V1.0
 **/
@Mapper
public interface UserMapper {
    /**
     * 查找所有用户
     *
     * @return
     */
    public List<User> findAll();
    
    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 返回查找的用户，如果查找用户为空则返回Null
     */
    public User findByUsername(@Param("username") String username);
    
    /**
     * 通过编号查找用户
     *
     * @param id
     * @return
     */
    public User findById(@Param("id") Integer id);
    
    /**
     * 更新用户状态
     *
     * @param id 用户编号
     * @param status 用户状态的编号
     */
    void updateUserStatus(@Param("id") int id, @Param("status") int status);
    
    /**
     * 更新用户信息
     * @Param user 需要更新的用户信息
     */
    void updateUser(User user);
    
    /**
     * 新增用户
     *
     * @param user
     */
    void addUser(User user);
}
