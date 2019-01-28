package com.liuqi.tools.codelife.controllers.rest;

import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * 用户管理Controller
 * 只有管理员能访问这其中的所有接口
 *
 * @Author: LiuQI
 * @Created: 2018/4/2 15:25
 * @Version: V1.0
 **/
@RequestMapping("/manager/user")
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
public class UserManagerController {
    @Autowired
    private UserService userService;
    
    /**
     * 获取所有用户
     *
     * @return
     */
    @RequestMapping("/getAll")
    public Collection<User> getAll() {
        return userService.findAll();
    }
    
    /**
     * 解锁用户
     *
     * @param id
     */
    @RequestMapping("/unlock")
    public void unlock(@RequestParam("id") Integer id) throws RestException {
        userService.unlockUser(id);
    }
    
    /**
     * 注销用户
     * 注销后用户无法再登录
     *
     * @param id
     */
    @RequestMapping("/unregister")
    public void unregister(@RequestParam("id")Integer id) throws RestException {
        userService.unregisterUser(id);
    }
    
    /**
     * 审核用户
     *
     * @param id
     */
    @RequestMapping("/approve")
    public void approve(@RequestParam("id") Integer id) throws RestException {
        userService.approveUser(id);
    }
}
