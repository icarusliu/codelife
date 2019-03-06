package com.liuqi.tools.codelife.web.thymeleaf;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.db.entity.*;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import com.liuqi.tools.codelife.service.*;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import com.liuqi.tools.codelife.util.SessionProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Collection;

/**
 * 文章控制器
 *
 * @Author: LiuQI
 * @Created: 2018/3/26 17:46
 * @Version: V1.0
 **/
@Controller
@RequestMapping("/article")
public class ArticleThymeleafController {
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private ArticleTypeService typeService;

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

    /**
     * 获取文章详细页面
     *
     * @return
     */
    @GetMapping("/detail")
    public ModelAndView articleDsetail(@RequestParam("id") Integer id, HttpSession session) throws RestException {
        Article article;
        try {
            article = articleService.findById(id);
        } catch (RestException ex) {
            return articles(null, null, null);
        }

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

    private static final String ARTICLE_READ_IDS = "article-read-ids";
}
