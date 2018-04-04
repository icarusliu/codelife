package com.liuqi.tools.codelife.controllers;

import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户Controller
 *
 * @Author: LiuQI
 * @Created: 2018/3/30 15:47
 * @Version: V1.0
 **/
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     */
    @RequestMapping("/userExists")
    @ResponseBody
    public boolean userExists(@RequestParam("username") String username) {
        try {
            userService.findByUsername(username);
            return true;
        } catch (RestException ex) {
            return false;
        }
    }
    
    @RequestMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    @PostMapping("/userRegister")
    @ResponseBody
    public String registerUser(@RequestParam(value = "username", required = true) String username,
                               @RequestParam(value = "password", required = true) String password,
                               @RequestParam(value = "realName", required = true) String realName) throws RestException {
        userService.register(username, password, realName);
        
        return "success";
    }
}
