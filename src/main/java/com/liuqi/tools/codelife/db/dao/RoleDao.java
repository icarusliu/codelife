package com.liuqi.tools.codelife.db.dao;

import com.liuqi.tools.codelife.entity.Role;

import java.util.Collection;

/**
 * 角色操作Dao
 *
 * @Author: LiuQI
 * @Created: 2018/3/24 16:00
 * @Version: V1.0
 **/
public interface RoleDao {
    /**
     * 查找所有角色
     *
     * @return 返回所有角色清单
     */
    public Collection<Role> findAllRoles();
    
    /**
     * 新增角色
     *
     * @param roleName
     * @param remark
     */
    void add(String roleName, String remark);
    
    /**
     * 更新角色
     *
     * @param id
     * @param roleName
     * @param remark
     */
    void update(Integer id, String roleName, String remark);
}
