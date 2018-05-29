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

import java.util.Map;

/**
 * 系统管理Controller
 *
 * @Author: LiuQI
 * @Created: 2018/3/25 23:01
 * @Version: V1.0
 **/
@Controller
@RequestMapping("/system")
public class SystemManagerController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @RequestMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TOPIC_ADMIN')")
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
