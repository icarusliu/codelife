package com.liuqi.tools.codelife.controllers.thymeleaf;

import com.liuqi.tools.codelife.service.RoleService;
import com.liuqi.tools.codelife.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统管理Controller
 *
 * @Author: LiuQI
 * @Created: 2018/3/25 23:01
 * @Version: V1.0
 **/
@Controller
@RequestMapping("/system")
public class SystemManagerThymeleafController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @RequestMapping
    @PreAuthorize("isAuthenticated()")
    public String system() {
        return "system/index";
    }
    
    /**
     * 角色管理界面
     *
     * @return
     */
    @RequestMapping({"/roleManager"})
    @PreAuthorize("hasAuthority('ADMIN')")
    public String roleManager() {
        return "system/roleManager";
    }
    
    /**
     * 用户管理界面
     *
     * @return
     */
    @RequestMapping("/userManager")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userManager() {
        return "system/userManager";
    }
    
    /**
     * 日志分析页面
     *
     * @return
     */
    @RequestMapping("/logStatistics")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String logStatistics() {
        return "system/logStatistics";
    }
}
