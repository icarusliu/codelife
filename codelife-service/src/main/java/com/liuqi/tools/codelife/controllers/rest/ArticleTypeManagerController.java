package com.liuqi.tools.codelife.controllers.rest;

import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleTypeService;
import com.liuqi.tools.codelife.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章分类管理控制器
 *
 * @Author: LiuQI
 * @Created: 2018/4/16 14:34
 * @Version: V1.0
 **/
@RestController
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
    public void add(@RequestParam("typeName") String name,
                         @RequestParam(value = "id", required = false) Integer id) throws RestException {
        if (null == id) {
            typeService.saveType(name);
        } else {
            typeService.saveType(id, name);
        }
    }

    /**
     * REST方式下文章分类管理界面中获取所有文章分类
     *
     * @return
     */
    @GetMapping("/getAllForManager")
    public List<ArticleType> getAllForManager() {
        return typeService.findByUser(authenticationService.getLoginUser());
    }
}
