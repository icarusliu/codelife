package com.liuqi.tools.codelife.controllers;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.CommentType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleService;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.CommentService;
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
    
    /**
     * 打开文章清单页面
     * 同时获取所有文章分类、所有文章
     * 所有用户均可以访问
     *
     * @return
     */
    @GetMapping("/articles")
    public ModelAndView articles(@RequestParam(name = "typeId", required = false) Integer typeId,
                                 @RequestParam(value = "nowPage", required = false) Integer nowPage,
                                 @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Collection<ArticleType> types = articleService.findAllTypes();
        PageInfo<Article> pageInfo;
        nowPage = null == nowPage ? 1 : nowPage;
        pageSize = null == pageSize ? 20 : pageSize;
        
        if (null != typeId) {
            pageInfo = articleService.findByType(typeId, nowPage, pageSize);
        } else {
            pageInfo = articleService.findAll(nowPage, pageSize);
        }

        return ModelAndViewBuilder.of("articles")
                .setData("types", types)
                .setData("typeId", typeId)
                .setData("articles", pageInfo.getList())
                .setData("pages", pageInfo.getPages())
                .setData("typeId", typeId)
                .setData("nowPage", nowPage)
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
    public List<Article> getAllArticles(@RequestParam(value = "nowPage", required = false) Integer nowPage,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return articleService.findAll(null == nowPage ? 1 : nowPage, null == pageSize ? 20 : pageSize).getList();
    }
    
    /**
     * 根据当前登录用户获取其有权限管理的文章清单
     *
     * @return
     * @throws RestException 如果用户不存在时抛出异常；实际上已经登录时理论上不会出现这种情况
     */
    @GetMapping("/getArticlesByAuthor")
    @ResponseBody
    public Map<String, Object> getArticlesByAuthor(@RequestParam(value = "offset", required = false) Integer offset,
                                                   @RequestParam(value = "limit", required = false) Integer pageSize)
            throws RestException {
        
        //offset与limit为datastrap table的分页参数
        int nowPage = (null == offset ? 0 : offset) / pageSize + 1;
        pageSize = null == pageSize ? 20 : pageSize;
        
        //用户只能看自己的文章，而管理员可以看所有的文章
        User user = authenticationService.getLoginUser();
        PageInfo<Article> pageInfo;
        if (user.isSystemAdmin()) {
            pageInfo = articleService.findAll(nowPage, pageSize);
        } else {
            pageInfo = articleService.findByAuthor(user, nowPage, pageSize);
        }
        
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
                .setData("types", articleService.findAllTypes())
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
        return articleService.searchTitle(key, 0, 20).getList();
    }
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private static final String ARTICLE_READ_IDS = "article-read-ids";
}
