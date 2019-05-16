package com.liuqi.tools.codelife.web.rest;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.db.entity.Article;
import com.liuqi.tools.codelife.db.entity.ArticleStatus;
import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.util.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.util.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleService;
import com.liuqi.tools.codelife.service.ArticleTypeService;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.util.MapBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文章管理Controller
 *
 * @Author: LiuQI
 * @Created: 2018/3/28 14:45
 * @Version: V1.0
 **/
@RestController
@RequestMapping("/article/manager")
@PreAuthorize("isAuthenticated()")
public class ArticleManagerController {
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private ArticleTypeService typeService;
    
    /**
     * 获取用于管理的文章清单
     * 根据当前登录用户获取其有权限管理的文章清单
     *
     * @return
     */
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(value = "offset", required = false) Integer offset,
                                                     @RequestParam(value = "limit", required = false) Integer pageSize) {
        
        //offset与limit为datastrap table的分页参数
        pageSize = null == pageSize ? 20 : pageSize;
        int nowPage = (null == offset ? 0 : offset) / pageSize + 1;
        
        //用户只能看自己的文章，而管理员可以看所有的文章
        User user = authenticationService.getLoginUser();
        PageInfo<Article> pageInfo = articleService.findForManager(user, nowPage, pageSize);
        
        Map<String, Object> map = new HashMap<>(2);
        map.put("rows", pageInfo.getList());
        map.put("total", pageInfo.getTotal());
        
        return map;
    }

    /**
     * REST方式下获取新建文章时需要使用到的数据
     *
     * @param articleId
     * @return
     * @throws RestException
     */
    @PostMapping("/getDataForNew")
    public Map<String, Object> getDataForNew(@RequestParam(value = "articleId", required = false) Integer articleId) throws RestException {
        User loginUser = authenticationService.getLoginUser();
        
        MapBuilder builder = MapBuilder.of()
                .put("types", typeService.findByUser(loginUser))
                .put("forums", typeService.findSystemTypes())
                .put("myTopics", topicService.getUserTopics(loginUser.getId()));
        
        if (null != articleId) {
            Article article = articleService.findById(articleId);
            builder.put("article", article);
        }
        
        return builder.build();
    }
    
    /**
     * 保存文章　
     * 如果ID不为空，则为更新，否则为新增
     * 限制只有作者才能保存文章
     *
     * @param title
     * @param type
     * @param content
     * @param id
     * @return
     * @throws RestException
     */
    @PostMapping("/save")
    public String save(@RequestParam("title") String title,
                              @RequestParam("type") Integer type,
                              @RequestParam("content") String content,
                              @RequestParam(value = "topic", required = false) Integer topicId,
                              @RequestParam(value = "forumId", required = false) Integer forumId,
                              @RequestParam(name = "id", required = false) Integer id,
                             @RequestParam(name = "fileIds", required = false) String fileIdsStr,
                             @RequestParam(name = "status", required = false) Integer status) throws RestException {
        List<Integer> fileIds = new ArrayList<>(16);
        if (StringUtils.isNotBlank(fileIdsStr)) {
            for (String s : fileIdsStr.split(",")) {
                fileIds.add(Integer.valueOf(s));
            }
        }
        if (null == id) {
            ArticleStatus articleStatus = ArticleStatus.APPROVED;
            if (null != status) {
                articleStatus = ArticleStatus.parse(status);
            }
            articleService.saveArticle(title, content, type, topicId, forumId, fileIds, articleStatus);
        } else {
            //判断登录用户是否是作者，如果不是则不能进行保存
            User loginUser = authenticationService.getLoginUser();
            Article article = articleService.findById(id);
            
            if (article.getAuthorID() != loginUser.getId()) {
                logger.error("User is not the author, user: {}!", loginUser.getUsername());
                throw ExceptionTool.getException(ErrorCodes.ARTICLE_MANAGER_EDIT_NOT_AUTHOR);
            }
            
            //只允许更新标题与内容
            articleService.updateArticle(id, title, content, type, fileIds);
        }
        
        return "succeed";
    }
    
    /**
     * 删除文章
     *
     * @param id 需要删除文章的ID
     */
    @PostMapping("/delete")
    public void delete(@RequestParam("id") Integer id) throws RestException {
        User loginUser = authenticationService.getLoginUser();

        Article article = articleService.findById(id);
        if (article.getAuthorID() != loginUser.getId() && !loginUser.isSystemAdmin()) {
            logger.error("User is not the author, user: {}!", loginUser.getUsername());
            throw ExceptionTool.getException(ErrorCodes.ARTICLE_MANAGER_DELETE_NOT_AUTHOR);
        }

        articleService.deleteArticle(id);
    }
    
    /**
     * 文章置顶
     * @param id
     */
    @PostMapping("/fixTop")
    public void fixTop(@RequestParam("id") Integer id) {
        //如果是管理员进行操作，则是进行推荐；否则是进行置顶（只会在用户页面中显示在前列）
        User user = authenticationService.getLoginUser();
        
        if (user.isSystemAdmin()) {
            articleService.recommend(id);
        } else {
            articleService.fixTop(id);
        }
    }
    
    /**
     * 文章取消置顶
     * @param id
     */
    @PostMapping("/unFixTop")
    public void unFixTop(@RequestParam("id") Integer id) {
        User user = authenticationService.getLoginUser();
    
        if (user.isSystemAdmin()) {
            articleService.unRecommend(id);
        } else {
            articleService.unFixTop(id);
        }
    }
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleManagerController.class);
}
