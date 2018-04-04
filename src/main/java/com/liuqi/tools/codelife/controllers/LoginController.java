package com.liuqi.tools.codelife.controllers;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.service.ArticleService;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * 登录处理Controller
 *
 * @Author: LiuQI
 * @Created: 2018/3/27 21:09
 * @Version: V1.0
 **/
@Controller
public class LoginController {
    @Autowired
    private ArticleService articleService;
    
    /**
     * 超时处理
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/timeout")
    public void sessionTimeout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with").equalsIgnoreCase(
                "XMLHttpRequest")) { // ajax 超时处理
            response.getWriter().print("timeout"); //设置超时标识
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
    @RequestMapping({"/index", "/", "/login"})
    public ModelAndView index() {
        Collection<Article> articles;
        try {
           articles  = articleService.findTopArticleNoContent(10);
        } catch (Exception ex) {
            articles = Collections.EMPTY_LIST;
        }
        
        return ModelAndViewBuilder.of("index")
                .setData("articles", articles)
                .build();
    }
    
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
