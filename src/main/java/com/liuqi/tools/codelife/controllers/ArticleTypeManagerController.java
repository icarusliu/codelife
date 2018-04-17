package com.liuqi.tools.codelife.controllers;

import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleTypeService;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

/**
 * 文章分类管理控制器
 *
 * @Author: LiuQI
 * @Created: 2018/4/16 14:34
 * @Version: V1.0
 **/
@Controller
@RequestMapping("/system/articleTypeManager")
@PreAuthorize("isAuthenticated()")
public class ArticleTypeManagerController {
    @Autowired
    private ArticleTypeService typeService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    /**
     * 保存分类
     * 如果ID不为空则是进行更新
     *
     * @param name
     * @param id 可能为空，不为空时是更新；为空时是新增
     */
    @PostMapping("/add")
    @ResponseBody
    public void add(@RequestParam("typeName") String name,
                         @RequestParam(value = "id", required = false) Integer id) throws RestException {
        if (null == id) {
            typeService.saveType(name);
        } else {
            typeService.saveType(id, name);
        }
    }
    
    /**
     * 打开文章分类管理页面
     * 只有登录用户才能访问
     *
     * @return
     */
    @GetMapping
    public ModelAndView typeManager() {
        User user = authenticationService.getLoginUser();
        
        Collection<ArticleType> types = typeService.findByUser(user);
        return ModelAndViewBuilder.of("articleManager/typeManager")
                .setData("types", types)
                .build();
    }
}
