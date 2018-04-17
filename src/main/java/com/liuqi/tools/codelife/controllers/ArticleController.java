package com.liuqi.tools.codelife.controllers;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.entity.*;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.*;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import com.liuqi.tools.codelife.util.SessionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private ArticleTypeService typeService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 打开文章清单页面
     * 同时获取所有文章分类、所有文章
     * 所有用户均可以访问
     *
     * @return
     */
    @GetMapping("/articles")
    public ModelAndView articles(@RequestParam(name = "forumId", required = false) Integer forumId,
                                 @RequestParam(value = "nowPage", required = false) Integer nowPage,
                                 @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Collection<ArticleType> forums = typeService.findSystemTypes();
        PageInfo<Article> pageInfo;
        nowPage = null == nowPage ? 1 : nowPage;
        pageSize = null == pageSize ? 20 : pageSize;
        
        if (null != forumId) {
            pageInfo = articleService.findForExplorer(forumId, nowPage, pageSize);
        } else {
            pageInfo = articleService.findForExplorer(nowPage, pageSize);
        }

        return ModelAndViewBuilder.of("articles")
                .setData("forums", forums)
                .setData("forumId", forumId)
                .setData("articles", pageInfo.getList())
                .setData("pages", pageInfo.getPages())
                .setData("nowPage", nowPage)
                .build();
    }
    
    @GetMapping("/getAllTypes")
    @ResponseBody
    public Collection<ArticleType> getAllTypes(@RequestParam(value = "userId", required = false) Integer userId) throws RestException {
        User user;
        if (null != userId) {
            user = userService.findById(userId);
        } else {
            user = authenticationService.getLoginUser();
        }
        return typeService.findByUser(user);
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
        int nowPage = (null == offset ? 0 : offset) / pageSize + 1;
        pageSize = null == pageSize ? 20 : pageSize;
        
        //用户只能看自己的文章，而管理员可以看所有的文章
        User user = authenticationService.getLoginUser();
        PageInfo<Article> pageInfo = articleService.findForManager(user, nowPage, pageSize);
        
        Map<String, Object> map = new HashMap<>();
        map.put("rows", pageInfo.getList());
        map.put("total", pageInfo.getTotal());
        
        return map;
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
                .setData("article", article)
                .setData("comments", commentService.findByDestination(CommentType.ARTICLE, id))
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
    
    /**
     * 根据标题关键字进行搜索
     *
     * @param key
     * @return
     */
    @PostMapping("/searchArticleByTitle")
    @ResponseBody
    public List<Article> searchTitle(@RequestParam("key") String key) {
        return articleService.search(key, 0, 20).getList();
    }
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private static final String ARTICLE_READ_IDS = "article-read-ids";
}
