package com.liuqi.tools.codelife.controllers;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.entity.UserArticleStatInfo;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.*;
import com.liuqi.tools.codelife.util.MapBuilder;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    
    @Autowired
    private AuthenticationProvider authenticationProvider;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    
    public UserController() {
    }
    
    @PostMapping("/user/search")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Collection<User> search(@RequestParam("key") String key) {
        return userService.search(key);
    }
    
    /**
     * 自定义登录验证 在前后端分离时进行登录验证使用
     *
     * @param username
     * @param password
     */
    @PostMapping("/customLogin")
    @ResponseBody
    public String login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpSession session,
                      HttpServletRequest request) throws RestException {
        username = username.trim();
        password = password.trim();
        if ("".equals(username)) {
            logger.error("Username cannot be empty!");
            throw new RestException("用户名不能为空！");
        }
    
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        } catch (AuthenticationException ex) {
            throw new RestException(ex.getMessage());
        } catch (Exception e) {
            logger.error("Build authenticationManager failed!", e);
            throw new RestException(e.getMessage());
        }
    
        return "succeed";
    }
    
    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("/customLogout")
    @ResponseBody
    public String customLogout() {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        SecurityContextHolder.clearContext();
        
        
        return "succeed";
    }
    
    /**
     * 验证用户是否已经登录
     * @return
     */
    @PostMapping("/userLogined")
    @ResponseBody
    public boolean userLogined() {
        return authenticationService.getLoginUser() != null;
    }
    
    /**
     * 获取登录的用户
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/getLoginUser")
    public User getLoginUser() {
        return authenticationService.getLoginUser();
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
    
    /**
     * REST方式下的用户首页数据获取
     *
     * @param userId
     * @param typeId
     * @param nowPage
     * @param pageSize
     * @return
     * @throws RestException
     */
    @PostMapping("/user/getIndexData")
    @ResponseBody
    public Map<String, Object> getUserIndexData(@RequestParam(value = "userId") Integer userId,
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
        
        if (null == statInfo) {
            statInfo = new UserArticleStatInfo();
        }
    
        Map<String, Object> statInfoMap = MapBuilder.of()
                .put("articleCount", totalArticles)
                .put("readCount", statInfo.getReadCount())
                .put("praiseCount", statInfo.getPraiseCount())
                .put("commentCount", commentService.getCommentCountByAuthor(userId))
                .build();
        
        return MapBuilder.of()
                .put("types", typeService.findByUser(user))
                .put("articles", articlePageInfo.getList())
                .put("pages", articlePageInfo.getPages())
                .put("statInfo", statInfoMap)
                .put("user", user)
                .build();
    }
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
}
