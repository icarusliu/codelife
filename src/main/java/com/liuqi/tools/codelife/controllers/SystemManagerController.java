package com.liuqi.tools.codelife.controllers;

import com.liuqi.tools.codelife.service.ArticleService;
import com.liuqi.tools.codelife.service.RoleService;
import com.liuqi.tools.codelife.service.UserService;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 系统管理Controller
 *
 * @Author: LiuQI
 * @Created: 2018/3/25 23:01
 * @Version: V1.0
 **/
@Controller
@RequestMapping("/system")
@PreAuthorize("hasAuthority('ADMIN')")
public class SystemManagerController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    /**
     * 系统管理默认进角色管理页面
     *
     * @return
     */
    @RequestMapping({"/", "/roleManager"})
    public String roleManager() {
        return "system/roleManager";
    }
    
    /**
     * 用户管理界面
     *
     * @return
     */
    @RequestMapping("/userManager")
    public String userManager() {
        return "system/userManager";
    }
    
    /**
     * 日志分析页面
     *
     * @return
     */
    @RequestMapping("/logStatistics")
    public String logStatistics() {
        return "system/logStatistics";
    }
    

}
