package com.liuqi.tools.codelife.controllers;

import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleTypeService;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * 文章分类控制器
 *
 * @Author: LiuQI
 * @Created: 2018/4/17 21:36
 * @Version: V1.0
 **/
@Controller
@RequestMapping("/articleType")
public class ArticleTypeController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private ArticleTypeService typeService;
    
    @PostMapping("/getForums")
    @ResponseBody
    public List<ArticleType> getForums() {
        return typeService.findSystemTypes();
    }
    
    @GetMapping("/getAll")
    @ResponseBody
    public Collection<ArticleType> getAll(@RequestParam(value = "userId", required = false) Integer userId) throws RestException {
        User user;
        if (null != userId) {
            user = userService.findById(userId);
        } else {
            user = authenticationService.getLoginUser();
        }
        return typeService.findByUser(user);
    }
}
