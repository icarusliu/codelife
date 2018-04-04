package com.liuqi.tools.codelife.controllers;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleService;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import com.liuqi.tools.codelife.util.SessionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

/**
 * 文章控制器
 *
 * @Author: LiuQI
 * @Created: 2018/3/26 17:46
 * @Version: V1.0
 **/
@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    /**
     * 打开文章清单页面
     * 同时获取所有文章分类、所有文章
     * 所有用户均可以访问
     *
     * @return
     */
    @GetMapping("/articles")
    public ModelAndView articles(@RequestParam(name = "typeId", required = false) Integer typeId) {
        Collection<ArticleType> types = articleService.findAllTypes();
        Collection<Article> articles;
        if (null != typeId) {
            articles = articleService.findByType(typeId);
        } else {
            articles = articleService.findAll();
        }

        return ModelAndViewBuilder.of("articles")
                .setData("types", types)
                .setData("typeId", typeId)
                .setData("articles", articles)
                .build();
    }
    
    @GetMapping("/getAllTypes")
    @ResponseBody
    public Collection<ArticleType> getAllTypes() {
        return articleService.findAllTypes();
    }
    
    /**
     * 获取所有文章清单
     *
     * @return 返回所有文章清单
     */
    @GetMapping("/getAllArticles")
    @ResponseBody
    public Collection<Article> getAllArticles() {
        return articleService.findAll();
    }
    
    /**
     * 根据当前登录用户获取其有权限管理的文章清单
     *
     * @return
     * @throws RestException 如果用户不存在时抛出异常；实际上已经登录时理论上不会出现这种情况
     */
    @GetMapping("/getArticlesByAuthor")
    @ResponseBody
    public Collection<Article> getArticlesByAuthor() throws RestException {
        //用户只能看自己的文章，而管理员可以看所有的文章
        User user = authenticationService.getLoginUser();
        Collection<Article> articles;
        if (user.isSystemAdmin()) {
            articles = articleService.findAll();
        } else {
            articles = articleService.findByAuthor(user);
        }
        
        return articles;
    }
    
    /**
     * 获取文章详细页面
     *
     * @return
     */
    @GetMapping("/articleDetail")
    public ModelAndView articleDetail(@RequestParam("id") Integer id, HttpSession session) throws RestException {
        Article article = articleService.findById(id);
        
        //阅读次数加1
        //一个Session只算一次；
        boolean notExist = SessionProxy.proxy(session)
                .notExistSetMapAttribute(ARTICLE_READ_IDS, String.valueOf(id), id);
        if (notExist) {
            articleService.addReadCount(article);
        }
        
        return ModelAndViewBuilder.of("articleDetail")
                .setData("types", articleService.findAllTypes())
                .setData("article", article)
                .build();
    }
    
    /**
     * 对文章进行点赞
     *
     * @param id 文章编号
     */
    @PostMapping("/praiseArticle")
    @ResponseBody
    public String praise(@RequestParam("id") Integer id) {
        articleService.praise(id);
        return "success";
    }
    
    /**
     * 取消对文章的点赞
     *
     * @param id 文章编号
     */
    @PostMapping("/unpraiseArticle")
    @ResponseBody
    public void unpraise(@RequestParam("id") Integer id) {
        articleService.unpraise(id);
    }
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private static final String ARTICLE_READ_IDS = "article-read-ids";
}
