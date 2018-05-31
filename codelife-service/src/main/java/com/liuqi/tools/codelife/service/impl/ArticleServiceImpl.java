package com.liuqi.tools.codelife.service.impl;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.db.dao.ArticleDao;
import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.entity.UserArticleStatInfo;
import com.liuqi.tools.codelife.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.*;
import com.liuqi.tools.codelife.tool.ArticleBuilder;
import com.liuqi.tools.codelife.tool.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 文章服务类的实现类
 *
 * @Author: LiuQI
 * @Created: 2018/3/26 17:26
 * @Version: V1.0
 **/
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleDao articleDao;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private TopicService topicService;
    
    @Value("${app.file.savePath}")
    private String contentFilePath;
    
    @Autowired
    private ArticleTypeService articleTypeService;
    
    @Override
    public Article findById(int id) throws RestException {
        Article article = articleDao.findById(id);
        article.setContent(FileUtils.getFileContent(article.getContentUrl(), contentFilePath));
        
        return article;
    }
    
    /**
     * 查找文章用于展现用
     * 按是否置顶等顺序进行排序
     *
     * @param nowPage
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Article> findForExplorer(int nowPage, int pageSize) {
        return PageInfo.of(articleDao.findForExplorerOrderByRecommended(null));
    }
    
    @Override
    public PageInfo<Article> findForExplorer(Integer forumId, int nowPage, int pageSize) {
        return PageInfo.of(articleDao.findForExplorerOrderByRecommended(forumId));
    }
    
    @Override
    public PageInfo<Article> findByAuthorForExplorer(User user, int nowPage, int pageSize) throws RestException {
        if (null == user) {
            logger.error("User cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "作者");
        }
        return PageInfo.of(articleDao.findForExplorerOrderByFixTop(user.getId(), null));
    }
    
    @Override
    public PageInfo<Article> findByAuthorForExplorer(User user, Integer typeId, int nowPage, int pageSize) throws RestException {
        if (null == user) {
            logger.error("User cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "作者");
        }
        
        return PageInfo.of(articleDao.findForExplorerOrderByFixTop(user.getId(), typeId));
    }
    
    @Override
    public PageInfo<Article> findForManager(User user, int nowPage, int pageSize) {
        if (null != user && user.isSystemAdmin()) {
            //如果是系统管理员，则能看到所有文章
            user = null;
        }
        
        return PageInfo.of(articleDao.findForManager(null == user ? null : user.getId(), null));
    }
    
    @Override
    public PageInfo<Article> findForManager(User user, Integer typeId, int nowPage, int pageSize) {
        if (null != user && user.isSystemAdmin()) {
            //如果是系统管理员，则能看到所有文章
            user = null;
        }
        
        return PageInfo.of(articleDao.findForManager(null == user ? null : user.getId(), typeId));
    }
    
    @Override
    public PageInfo<Article> search(String key, int nowPage, int pageSize) {
        if (null == key) {
            return new PageInfo(Collections.EMPTY_LIST);
        }
        return new PageInfo(articleDao.search(key));
    }
    
    @Override
    public void saveArticle(String title, String content, Integer type, Integer topicId, Integer forumId) throws RestException {
        if (null == title || "".equals(title.trim())) {
            logger.error("Title cannot be null or empty");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "文章标题");
        }
    
        if (null == content || "".equals(content)) {
            logger.error("Content cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "文章内容");
        }
        
        //type不能为空
        if (null == type) {
            logger.error("Type cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "文章分类");
        }
        
        User user = authenticationService.getLoginUser();
        
        Article article = ArticleBuilder.of(title)
                .setType(articleTypeService.findById(type))
                .setContent(content, contentFilePath)
                .setAuthor(user)
                .build();
        if (null != forumId) {
            article.setForum(articleTypeService.findById(forumId));
        }
        articleDao.save(article);
        
        //文章分类对象中文章数目加1
        articleTypeService.addArticleCount(topicId);
        
        //将文章添加到专题
        if (null != topicId && 0 != topicId) {
            topicService.addTopicArticls(topicId, List.of(article.getId()));
        }
    }
    
    @Override
    public void addReadCount(Article article) {
        articleDao.addReadCount(article.getId());
    }
    
    /**
     * 根据ID删除文章
     *
     * @param id
     */
    @Override
    public void deleteArticle(Integer id) {
        articleDao.deleteArticle(id);
        
        //分类中文章数目减1
        articleTypeService.deduceArticleCount(id);
    }
    
    /**
     * 更新文章
     *  @param id
     * @param title
     * @param content
     */
    @Override
    public void updateArticle(Integer id, String title, String content, Integer type) throws RestException {
        if (null == id) {
            logger.error("Id cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "文章编号");
        }
        
        if (null == title || "".equals(title.trim())) {
            logger.error("Title cannot be null or empty");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "文章标题");
        }
        
        if (null == content || "".equals(content)) {
            logger.error("Content cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "文章内容");
        }
        
        //type不能为空
        if (null == type) {
            logger.error("Type cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "文章分类");
        }
        
        Article article = articleDao.findById(id);
        if (null == article) {
            logger.error("Article with the special id does not exist!");
            throw ExceptionTool.getException(ErrorCodes.ARTICLE_NOT_EXISTS, id);
        }
        
        ArticleType oldType = article.getArticleType();
        
        //更新文件内容
        FileUtils.saveToFile(content, contentFilePath, article.getContentUrl());
        
        //更新数据库信息 版块不进行更新
        articleDao.updateArticle(id, title, type, article.getForum().getId());
        
        //更新分类中的文章计数信息
        if (oldType.getId() != type) {
            articleTypeService.addArticleCount(type);
            articleTypeService.deduceArticleCount(oldType.getId());
        }
    }
    
    @Override
    public void praise(int id) {
        //不检查文章是否存在
        //更新失败对用户无影响
        articleDao.addPraiseCount(id, 1);
    }
    
    @Override
    public void unpraise(int id) {
        //不检查文章是否存在
        //更新失败对用户无影响
        articleDao.addPraiseCount(id, -1);
    }
    
    @Override
    public void fixTop(Integer id) {
        articleDao.updateFixTop(id, true);
    }
    
    @Override
    public void unFixTop(Integer id) {
        articleDao.updateFixTop(id, false);
    }
    
    @Override
    public void recommend(Integer id) {
        articleDao.updateRecommended(id, true);
    }
    
    @Override
    public void unRecommend(Integer id) {
        articleDao.updateRecommended(id, false);
    }
    
    @Override
    public UserArticleStatInfo getStatisticInfoByAuthor(int authorId) {
        return articleDao.getStatisticInfoByAuthor(authorId);
    }
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
}
