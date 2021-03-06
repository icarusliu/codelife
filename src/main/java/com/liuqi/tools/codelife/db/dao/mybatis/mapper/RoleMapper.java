package com.liuqi.tools.codelife.db.dao.mybatis.mapper;

import com.liuqi.tools.codelife.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;

/**
 * 角色操作Mapper
 */
@Mapper
public interface RoleMapper {
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
