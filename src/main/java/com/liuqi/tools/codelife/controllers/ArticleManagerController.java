package com.liuqi.tools.codelife.controllers;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleService;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

/**
 * 文章管理Controller
 *
 * @Author: LiuQI
 * @Created: 2018/3/28 14:45
 * @Version: V1.0
 **/
@Controller
@RequestMapping("/articleManager")
public class ArticleManagerController {
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private TopicService topicService;
    
    /**
     * 打开文章管理页面
     * 同时获取所有文章分类、所有文章
     * 只有登录用户才可访问
     * 用户只能查看自己发表的文章；而管理员可以看到所有文章 ；
     *
     * @return
     */
    @GetMapping("/")
    public ModelAndView articleManager() throws RestException {
        Collection<ArticleType> types = articleService.findAllTypes();
        
        return ModelAndViewBuilder.of("articleManager/articleManager")
                .setData("types", types)
                .build();
    }
    
    /**
     * 打开文章分类管理页面
     * 只有登录用户才能访问
     * 暂只有管理员才能进行分类管理，后续可考虑支持每个人定制自己的分类
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/typeManager")
    public ModelAndView typeManager() {
        Collection<ArticleType> types = articleService.findAllTypes();
        return ModelAndViewBuilder.of("articleManager/typeManager")
                .setData("types", types)
                .build();
    }
    
    /**
     * 新建文章页面
     * 如果打开新建文章页面时传入了文章ID，那么对文章进行编辑而不是新建
     * @return
     */
    @GetMapping("/newArticle")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView newArticle(@RequestParam(name = "id",  required = false) Integer id) {
        User loginUser = authenticationService.getLoginUser();
        
        ModelAndViewBuilder builder = ModelAndViewBuilder.of("articleManager/newArticle")
                .setData("types", articleService.findAllTypes())
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
    
    /**
     * 保存文章　
     * 如果ID不为空，则为更新，否则为新增
     * 限制只有作者才能保存文章
     *
     * @param title
     * @param type
     * @param content
     * @param id
     * @return
     * @throws RestException
     */
    @PostMapping("/saveArticle")
    @ResponseBody
    public String saveArticle(@RequestParam("title") String title, @RequestParam("type") Integer type,
                              @RequestParam("content") String content,
                              @RequestParam(value = "topic", required = false) Integer topicId,
                              @RequestParam(name = "id", required = false) Integer id) throws RestException {
        if (null == id) {
            articleService.saveArticle(title, content, type, topicId);
        } else {
            //判断登录用户是否是作者，如果不是则不能进行保存
            User loginUser = authenticationService.getLoginUser();
            Article article = articleService.findById(id);
            
            if (article.getAuthorID() != loginUser.getId()) {
                logger.error("User is not the author, user: {}!", loginUser.getUsername());
                throw new RestException("只能修改自己发布的文章！");
            }
            
            articleService.updateArticle(id, title, content, type);
        }
        
        return "succeed";
    }
    
    /**
     * 删除文章
     *
     * @param id 需要删除文章的ID
     */
    @PostMapping("/deleteArticle")
    @ResponseBody
    public void deleteArticle(@RequestParam("id") Integer id) throws RestException {
        User loginUser = authenticationService.getLoginUser();
        Article article = articleService.findById(id);
    
        if (article.getAuthorID() != loginUser.getId()) {
            logger.error("User is not the author, user: {}!", loginUser.getUsername());
            throw new RestException("不能删除他人发布的文章！");
        }
        
        articleService.deleteArticle(id);
    }
    
    /**
     * 保存分类
     * 如果ID不为空则是进行更新
     * 只能由系统管理员进行管理
     *
     * @param name
     * @param id 可能为空，不为空时是更新；为空时是新增
     */
    @PostMapping("/saveType")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveType(@RequestParam("typeName") String name,
                         @RequestParam(value = "id", required = false) Integer id) throws RestException {
        if (null == id) {
            articleService.saveType(name);
        } else {
            articleService.saveType(id, name);
        }
    }
    
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleManagerController.class);
}
