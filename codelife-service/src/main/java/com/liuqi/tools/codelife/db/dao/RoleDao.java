package com.liuqi.tools.codelife.db.dao;

import com.liuqi.tools.codelife.db.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 角色操作Mapper
 * @author qi.liu
 */
@Mapper
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
    void add(@Param("roleName") String roleName, @Param("remark") String remark);
    
    /**
     * 更新角色
     *
     * @param id
     * @param roleName
     * @param remark
     */
    void update(@Param("id") Integer id, @Param("roleName") String roleName, @Param("remark") String remark);
}
