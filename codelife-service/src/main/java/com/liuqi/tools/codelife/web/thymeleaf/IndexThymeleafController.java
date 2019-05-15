package com.liuqi.tools.codelife.web.thymeleaf;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.db.entity.Article;
import com.liuqi.tools.codelife.db.entity.Topic;
import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.service.ArticleService;
import com.liuqi.tools.codelife.service.ArticleTypeService;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 首页处理控制器
 *
 * @Author: LiuQI
 * @Created: 2018/3/27 21:09
 * @Version: V1.0
 **/
@Controller
public class IndexThymeleafController {
    private static final String XMLHTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "x-requested-with";
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ArticleTypeService articleTypeService;
    
    /**
     * 超时处理
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/timeout")
    public void sessionTimeout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ajax 超时处理
        if (XMLHTTP_REQUEST.equalsIgnoreCase(request.getHeader(X_REQUESTED_WITH))) {
            //设置超时标识
            response.getWriter().print("timeout");
            response.getWriter().close();
        } else {
            response.sendRedirect("/index");
        }
    }
    
    /**
     * 首页请求
     * 同时返回以下数据：
     * 10个不含有文章内容的文章清单（做显示用，因此无需文章内容）；
     *
     * @return
     */
//    @RequestMapping({"/index", "/"})
    public ModelAndView index() {
        List articles;
        try {
            articles  = articleService.findForExplorer(1, 20).getList();
        } catch (Exception ex) {
            articles = Collections.EMPTY_LIST;
        }

        //获取订阅专题
        //获取用户订阅专题及其更新的文章
        User loginUser = authenticationService.getLoginUser();
        List myTopics;
        if (null != loginUser) {
            myTopics = topicService.getUserTopics(loginUser.getId());
        } else {
            myTopics = Collections.EMPTY_LIST;
        }

        return ModelAndViewBuilder.of("index")
                .setData("articles", articles)
                .setData("topics", topicService.search("", 1, 20).getList())
                .setData("articleTypes", articleTypeService.findSystemTypes())
                .setData("myTopics", myTopics)
                .build();
    }
    
    /**
     * 关键字搜索
     * 目前通过SQL LIKE的方式搜索专题与文章
     * TODO 通过Lucense来进行全文检索
     * @param key
     * @return
     */
    @GetMapping("/search")
    public ModelAndView search(@RequestParam("key") String key,
                               @RequestParam(value = "nowPage", required = false) Integer nowPage,
                               @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        nowPage = null == nowPage ? 1 : nowPage;
        pageSize = null == pageSize ? 20 : pageSize;
        
        //专题最多搜索10个
        PageInfo<Topic> topics = topicService.search(key, 1, 10);
        PageInfo<Article> articles = articleService.search(key, nowPage, pageSize);
        
        return ModelAndViewBuilder.of("search")
                .setData("articles", articles.getList())
                .setData("nowPage", (nowPage))
                .setData("pages", articles.getPages())
                .setData("topics", topics.getList())
                .setData("key", key)
                .build();
    }
    
    @RequestMapping("/about")
    public String about(){
        return "about";
    }
    
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
