package com.liuqi.tools.codelife.service.impl;

import com.liuqi.tools.codelife.db.dao.RoleDao;
import com.liuqi.tools.codelife.entity.Role;
import com.liuqi.tools.codelife.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Author: LiuQI
 * @Created: 2018/3/31 18:48
 * @Version: V1.0
 **/
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    
    @Override
    public Collection<Role> findAllRoles() {
        return roleDao.findAllRoles();
    }
    
    @Override
    public void add(String roleName, String remark) throws RestException {
        roleNameExists(roleName, null);
        roleName = roleName.trim();
        remark = remark.trim();
        
        if ("".equals(roleName)) {
            logger.error("Role name cannot be empty!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "角色名称");
        }
        
        roleDao.add(roleName, remark);
    }
    
    @Override
    public void update(Integer id, String roleName, String remark) throws RestException {
        roleNameExists(roleName, id);
        
        roleName = roleName.trim();
        remark = remark.trim();
    
        if ("".equals(roleName)) {
            logger.error("Role name cannot be empty!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "角色名称");
        }
        
        roleDao.update(id, roleName, remark);
    }
    
    /**
     * 判断角色名称是否存在
     *
     * @param roleName
     * @param id
     * @throws RestException 当角色名称存在时抛出异常
     */
    private void roleNameExists(String roleName, Integer id) throws RestException {
        Collection<Role> roles = roleDao.findAllRoles();
        for (Role role: roles) {
            if (null != id && role.getId() != id && role.getName().equals(roleName)) {
                //如果编号不一样但名称一样，则表明有重复
                logger.error("Role name already exists, role name: {}", roleName);
                throw ExceptionTool.getException(ErrorCodes.ROLE_EXISTS);
            }
        }
    }
    
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
}
