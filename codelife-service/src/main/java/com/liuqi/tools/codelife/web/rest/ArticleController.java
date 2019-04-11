package com.liuqi.tools.codelife.web.rest;

import com.liuqi.tools.codelife.db.entity.Article;
import com.liuqi.tools.codelife.db.entity.CommentType;
import com.liuqi.tools.codelife.service.FileInfoService;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleService;
import com.liuqi.tools.codelife.service.CommentService;
import com.liuqi.tools.codelife.util.MapBuilder;
import com.liuqi.tools.codelife.util.SessionProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static com.liuqi.tools.codelife.util.AppConstants.MODULE_ARTICLE;

/**
 * 文章控制器
 *
 * @author LiuQI 2018/3/26 17:46
 * @version V1.0
 **/
@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private FileInfoService fileInfoService;
    
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
        
        return MapBuilder.of()
                .put("article", article)
                .put("comments", commentService.findByDestination(CommentType.ARTICLE, articleId))
                .put("files", fileInfoService.findByItem(MODULE_ARTICLE, articleId))
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
    
    private static final String ARTICLE_READ_IDS = "article-read-ids";
}
