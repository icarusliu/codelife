package com.liuqi.tools.codelife.web.thymeleaf;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.db.entity.Article;
import com.liuqi.tools.codelife.db.entity.ArticleType;
import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.db.entity.UserArticleStatInfo;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import com.liuqi.tools.codelife.service.*;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 用户Controller
 *
 * @Author: LiuQI
 * @Created: 2018/3/30 15:47
 * @Version: V1.0
 **/
@Controller
public class UserThymeleafController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private ArticleTypeService typeService;
    
    @Autowired
    private ArticleCommentService commentService;
    
    @Autowired
    private AuthenticationProvider authenticationProvider;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    
    public UserThymeleafController() {
    }
    
    @RequestMapping("/register")
    public String registerPage() {
        return "register";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
    
    /**
     * 用户首页
     * 显示用户的文章等信息
     *
     * @return
     */
    @GetMapping("/userIndex")
    public ModelAndView userIndex(@RequestParam(value = "userId") Integer userId,
                                  @RequestParam(value = "typeId", required = false) Integer typeId,
                                  @RequestParam(value = "nowPage", required = false) Integer nowPage,
                                  @RequestParam(value = "pageSize", required = false) Integer pageSize) throws RestException {
        nowPage = null == nowPage ? 1 : nowPage;
        pageSize = null == pageSize ? 20 : pageSize;
        
        User user = userService.findById(userId);
        
        PageInfo<Article> articlePageInfo = articleService.findByAuthorForExplorer(user, typeId, nowPage, pageSize);
    
        //查找所有用户分类并计算所有分类的文章总数
        List<ArticleType> types = typeService.findByUser(user);
        int totalArticles = types.parallelStream().map(t -> t.getArticleCount())
                .reduce((t1, t2) -> t1 + t2).orElse(0);
        
        //获取用户的评论数、点赞数等数据
        UserArticleStatInfo statInfo = articleService.getStatisticInfoByAuthor(userId);
        
        return ModelAndViewBuilder.of("userIndex")
                .setData("types", typeService.findByUser(user))
                .setData("articles", articlePageInfo.getList())
                .setData("totalArticles", totalArticles)
                .setData("pages", articlePageInfo.getPages())
                .setData("nowPage", nowPage)
                .setData("pageSize", pageSize)
                .setData("typeId", typeId)
                .setData("realName", user.getRealName())
                .setData("userId", userId)
                .setData("statInfo", statInfo)
                .setData("commentCount", commentService.getCommentCountByAuthor(userId))
                .build();
    }
    
}
