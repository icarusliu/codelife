package com.liuqi.tools.codelife.controllers.thymeleaf;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleService;
import com.liuqi.tools.codelife.service.ArticleTypeService;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.tool.ModelAndViewBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 文章管理Controller
 *
 * @Author: LiuQI
 * @Created: 2018/3/28 14:45
 * @Version: V1.0
 **/
@Controller
@RequestMapping("/system/articleManager")
@PreAuthorize("isAuthenticated()")
public class ArticleManagerThymeleafController {
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private ArticleTypeService typeService;
    
    /**
     * 打开文章管理页面
     * 同时获取所有文章分类、所有文章
     * 只有登录用户才可访问
     * 用户只能查看自己发表的文章；而管理员可以看到所有文章 ；
     *
     * @return
     */
    @GetMapping
    public ModelAndView articleManager() {
        return ModelAndViewBuilder.of("articleManager/articleManager")
                .build();
    }

    /**
     * 新建文章页面
     * 如果打开新建文章页面时传入了文章ID，那么对文章进行编辑而不是新建
     * @return
     */
    @GetMapping("/newArticle")
    public ModelAndView newArticle(@RequestParam(name = "id",  required = false) Integer id) {
        User loginUser = authenticationService.getLoginUser();
        
        ModelAndViewBuilder builder = ModelAndViewBuilder.of("articleManager/newArticle")
                .setData("types", typeService.findByUser(loginUser))
                .setData("forums", typeService.findSystemTypes())
                .setData("myTopics", topicService.getUserTopics(loginUser.getId()));
        
        if (null != id) {
            Article article = null;
            try {
                article = articleService.findById(id);
            } catch (RestException e) {
                logger.error("Article with the special id({}) does not exist!", e);
            }
            if (null != article) {
                builder.setData("article", article);
            }
        }
        
        return builder.build();
    }

    private static final Logger logger = LoggerFactory.getLogger(ArticleManagerThymeleafController.class);
}
