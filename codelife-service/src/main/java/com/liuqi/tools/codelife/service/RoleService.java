package com.liuqi.tools.codelife.service;

import com.liuqi.tools.codelife.entity.Role;
import com.liuqi.tools.codelife.exceptions.RestException;

import java.util.Collection;

/**
 * 权限服务接口
 *
 * @Author: LiuQI
 * @Created: 2018/3/31 18:46
 * @Version: V1.0
 **/
public interface RoleService {
    /**
     * 获取所有角色清单
     * 实现类需要保证返回的是非空的对象 在无角色时需要返回一个空的List
     * @return
     */
    Collection<Role> findAllRoles();
    
    /**
     * 新增角色
     *
     * @param roleName
     * @param remark
     * @throws RestException
     */
    void add(String roleName, String remark) throws RestException;
    
    /**
     * 更新角色
     * @param id
     * @param roleName
     * @param remark
     * @throws RestException
     */
    void update(Integer id, String roleName, String remark) throws RestException;
}
