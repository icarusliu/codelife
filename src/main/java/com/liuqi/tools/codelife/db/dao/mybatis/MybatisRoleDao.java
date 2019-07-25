package com.liuqi.tools.codelife.db.dao.mybatis;

import com.liuqi.tools.codelife.db.dao.RoleDao;
import com.liuqi.tools.codelife.db.dao.mybatis.mapper.RoleMapper;
import com.liuqi.tools.codelife.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * 角色DaoMyBatis实现类
 *
 * @Author: LiuQI
 * @Created: 2018/3/24 16:01
 * @Version: V1.0
 **/
@Repository
public class MybatisRoleDao implements RoleDao {
    @Autowired
    private RoleMapper roleMapper;
    
    @Override
    public Collection<Role> findAllRoles() {
        return Optional.ofNullable(roleMapper.findAllRoles()).orElse(Collections.EMPTY_LIST);
    }
    
    /**
     * 新增角色
     *
     * @param roleName
     * @param remark
     */
    @Override
    public void add(String roleName, String remark) {
        roleMapper.add(roleName, remark);
    }
    
    /**
     * 更新角色
     *
     * @param id
     * @param roleName
     * @param remark
     */
    @Override
    public void update(Integer id, String roleName, String remark) {
        roleMapper.update(id, roleName, remark);
    }
}
