package com.liuqi.tools.codelife.controllers;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.entity.UserArticleStatInfo;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.*;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;

/**
 * 用户Controller
 *
 * @Author: LiuQI
 * @Created: 2018/3/30 15:47
 * @Version: V1.0
 **/
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private ArticleTypeService typeService;
    
    @Autowired
    private CommentService commentService;
    
    @PostMapping("/user/search")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Collection<User> search(@RequestParam("key") String key) {
        return userService.search(key);
    }
    
    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     */
    @RequestMapping("/userExists")
    @ResponseBody
    public boolean userExists(@RequestParam("username") String username) {
        try {
            userService.findByUsername(username);
            return true;
        } catch (RestException ex) {
            return false;
        }
    }
    
    @RequestMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    @PostMapping("/userRegister")
    @ResponseBody
    public String registerUser(@RequestParam(value = "username", required = true) String username,
                               @RequestParam(value = "password", required = true) String password,
                               @RequestParam(value = "realName", required = true) String realName) throws RestException {
        userService.register(username, password, realName);
        
        return "success";
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
