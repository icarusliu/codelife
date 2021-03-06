package com.liuqi.tools.codelife.controllers;

import com.liuqi.tools.codelife.entity.Role;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * 角色Controller
 *
 * @Author: LiuQI
 * @Created: 2018/3/31 18:52
 * @Version: V1.0
 **/
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    
    /**
     * 只有管理员能看到所有角色
     *
     * @return
     */
    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Role> getAllRoles() {
        return roleService.findAllRoles();
    }
 
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveRole(@RequestParam("roleName") String roleName,
                           @RequestParam("remark") String remark,
                           @RequestParam(value = "id", required = false) Integer id) throws RestException {
        if (null != id) {
            roleService.update(id, roleName, remark);
        } else {
            roleService.add(roleName, remark);
        }
        
        return "success!";
    }
}
