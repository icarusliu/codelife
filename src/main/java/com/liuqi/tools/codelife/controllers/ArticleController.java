package com.liuqi.tools.codelife.controllers;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.entity.*;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.*;
import com.liuqi.tools.codelife.util.MapBuilder;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import com.liuqi.tools.codelife.util.SessionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
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
@RequestMapping("/article")
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
    
    @Resource
    private TopicService topicService;
    
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
    
    @PostMapping("/getForExplorer")
    @ResponseBody
    public List<Article> getForExplorer(@RequestParam(value = "forumId", required = false) Integer forumId,
                                        @RequestParam("nowPage") Integer nowPage,
                                                @RequestParam("pageSize") Integer pageSize) {
        if (null == forumId) {
            return articleService.findForExplorer(nowPage, pageSize).getList();
        } else {
            return articleService.findForExplorer(forumId, nowPage, pageSize).getList();
        }
    }
    
    /**
     * 获取文章详细信息
     *
     * @param articleId
     * @param session
     * @return
     * @throws RestException
     */
    @PostMapping("/getDetail")
    @ResponseBody
    public Map<String, Object> getArticleDetail(@RequestParam("id") Integer articleId, HttpSession session) throws RestException {
        Article article = articleService.findById(articleId);
    
        //阅读次数加1
        //一个Session只算一次；
        boolean notExist = SessionProxy.proxy(session)
                .notExistSetMapAttribute(ARTICLE_READ_IDS, String.valueOf(articleId), articleId);
        if (notExist) {
            articleService.addReadCount(article);
        }
        
        List<Topic> topics = topicService.findByArticle(articleId);
        
        return MapBuilder.of()
                .put("article", article)
                .put("comments", commentService.findByDestination(CommentType.ARTICLE, articleId))
                .put("topics", topics)
                .build();
    }
    
    /**
     * 获取文章详细页面
     *
     * @return
     */
    @GetMapping("/detail")
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
                .setData("topics", topicService.findByArticle(id))
                .build();
    }
    
    /**
     * 对文章进行点赞
     *
     * @param id 文章编号
     */
    @PostMapping("/praise")
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
    @PostMapping("/unpraise")
    @ResponseBody
    public void unpraise(@RequestParam("id") Integer id) {
        articleService.unpraise(id);
    }
    
    /**
     * 根据关键字进行搜索
     *
     * @param key
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    public List<Article> search(@RequestParam("key") String key) {
        return articleService.search(key, 0, 20).getList();
    }
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private static final String ARTICLE_READ_IDS = "article-read-ids";
}
