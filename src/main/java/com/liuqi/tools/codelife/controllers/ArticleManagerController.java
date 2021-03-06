package com.liuqi.tools.codelife.controllers;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleService;
import com.liuqi.tools.codelife.service.ArticleTypeService;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.util.MapBuilder;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
public class ArticleManagerController {
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
    public ModelAndView articleManager() throws RestException {
        return ModelAndViewBuilder.of("articleManager/articleManager")
                .build();
    }
    
    /**
     * 获取用于管理的文章清单
     * 根据当前登录用户获取其有权限管理的文章清单
     *
     * @return
     * @throws RestException 如果用户不存在时抛出异常；实际上已经登录时理论上不会出现这种情况
     */
    @GetMapping("/getArticlesForManager")
    @ResponseBody
    public Map<String, Object> getArticlesForManager(@RequestParam(value = "offset", required = false) Integer offset,
                                                     @RequestParam(value = "limit", required = false) Integer pageSize)
            throws RestException {
        
        //offset与limit为datastrap table的分页参数
        pageSize = null == pageSize ? 20 : pageSize;
        int nowPage = (null == offset ? 0 : offset) / pageSize + 1;
        
        //用户只能看自己的文章，而管理员可以看所有的文章
        User user = authenticationService.getLoginUser();
        PageInfo<Article> pageInfo = articleService.findForManager(user, nowPage, pageSize);
        
        Map<String, Object> map = new HashMap<>();
        map.put("rows", pageInfo.getList());
        map.put("total", pageInfo.getTotal());
        
        return map;
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
    
    /**
     * REST方式下获取新建文章时需要使用到的数据
     *
     * @param articleId
     * @return
     * @throws RestException
     */
    @PostMapping("/getNewArticleDatas")
    @ResponseBody
    public Map<String, Object> getNewArticleDatas(@RequestParam(value = "articleId", required = false) Integer articleId) throws RestException {
        User loginUser = authenticationService.getLoginUser();
        
        MapBuilder builder = MapBuilder.of()
                .put("types", typeService.findByUser(loginUser))
                .put("forums", typeService.findSystemTypes())
                .put("myTopics", topicService.getUserTopics(loginUser.getId()));
        
        if (null != articleId) {
            Article article = articleService.findById(articleId);
            builder.put("article", article);
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
    public String saveArticle(@RequestParam("title") String title,
                              @RequestParam("type") Integer type,
                              @RequestParam("content") String content,
                              @RequestParam(value = "topic", required = false) Integer topicId,
                              @RequestParam(value = "forumId", required = false) Integer forumId,
                              @RequestParam(name = "id", required = false) Integer id) throws RestException {
        if (null == id) {
            articleService.saveArticle(title, content, type, topicId, forumId);
        } else {
            //判断登录用户是否是作者，如果不是则不能进行保存
            User loginUser = authenticationService.getLoginUser();
            Article article = articleService.findById(id);
            
            if (article.getAuthorID() != loginUser.getId()) {
                logger.error("User is not the author, user: {}!", loginUser.getUsername());
                throw new RestException("只能修改自己发布的文章！");
            }
            
            //只允许更新标题与内容
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
     * 文章置顶
     * @param id
     */
    @PostMapping("/fixTop")
    @ResponseBody
    public void fixTop(@RequestParam("id") Integer id) {
        //如果是管理员进行操作，则是进行推荐；否则是进行置顶（只会在用户页面中显示在前列）
        User user = authenticationService.getLoginUser();
        
        if (user.isSystemAdmin()) {
            articleService.recommend(id);
        } else {
            articleService.fixTop(id);
        }
    }
    
    /**
     * 文章取消置顶
     * @param id
     */
    @PostMapping("/unFixTop")
    @ResponseBody
    public void unFixTop(@RequestParam("id") Integer id) {
        User user = authenticationService.getLoginUser();
    
        if (user.isSystemAdmin()) {
            articleService.unRecommend(id);
        } else {
            articleService.unFixTop(id);
        }
    }
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleManagerController.class);
}
