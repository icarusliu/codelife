package com.liuqi.tools.codelife.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.db.dao.ArticleDao;
import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.entity.UserArticleStatInfo;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.*;
import com.liuqi.tools.codelife.util.ArticleBuilder;
import com.liuqi.tools.codelife.util.FileUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public PageInfo<Article> findAll(int nowPage, int pageSize) {
        PageHelper.startPage(nowPage, pageSize);
        
        List<Article> articles = articleDao.findAll();
        
        return new PageInfo(articles);
    }
    
    @Override
    public Article findById(int id) throws RestException {
        Article article = articleDao.findById(id);
        article.setContent(FileUtils.getFileContent(article.getContentUrl(), contentFilePath));
        
        return article;
    }
    
    @Override
    public void saveArticle(String title, String content, int type, Integer topicId, Integer forumId) throws RestException {
        User user = authenticationService.getLoginUser();
        
        Article article = ArticleBuilder.of(title)
                .setType(articleTypeService.findById(type))
                .setContent(content, contentFilePath)
                .setForum(articleTypeService.findById(forumId))
                .setAuthor(user)
                .build();
        articleDao.save(article);
        
        //文章分类对象中文章数目加1
        articleTypeService.addArticleCount(topicId);
        
        //将文章添加到专题
        if (null != topicId && 0 != topicId) {
            topicService.addTopicArticls(topicId, List.of(article.getId()));
        }
    }
    
    @Override
    public PageInfo<Article> findByForum(Integer id, int nowPage, Integer pageSize) {
        PageHelper.startPage(nowPage, pageSize);
    
        return new PageInfo(articleDao.findByForum(id));
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
     * @param type
     * @param forumId
     */
    @Override
    public void updateArticle(Integer id, String title, String content, Integer type, Integer forumId) throws RestException {
        Article article = articleDao.findById(id);
        ArticleType oldType = article.getArticleType();
        
        //更新文件内容
        FileUtils.saveToFile(content, contentFilePath, article.getContentUrl());
        
        //更新数据库信息
        articleDao.updateArticle(id, title, type, forumId);
        
        //如果分类有修改，则修改两个分类下的文章数目
        if (null != type && oldType.getId() != type) {
            articleTypeService.deduceArticleCount(oldType.getId());
            articleTypeService.addArticleCount(type);
        }
    }
    
    /**
     * 按ReadCount及CreateDate排序返回指定个数的文章清单
     * 不包含有文件内容
     *
     * @param i 需要返回的文章个数
     * @return 如果没有一个文章则返回一个空的清单
     */
    @Override
    public List<Article> findTopArticleNoContent(int i) {
        Assert.assertNotEquals(0, i);
        
        return Optional.ofNullable(articleDao.findTopArticles(i)).orElse(Collections.EMPTY_LIST);
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
    
    /**
     * 通过用户查找它所发表的所有文章
     *
     * @param user
     * @param typeId
     * @return 如果未发表过文章时返回空的List
     */
    @Override
    public PageInfo<Article> findByAuthor(User user, int nowPage, int pageSize, Integer typeId) throws RestException {
        PageHelper.startPage(nowPage, pageSize);
        if (null == user || 0 == user.getId()) {
            logger.error("User or the id of user is null!");
            throw new RestException("用户或用户编号为空！");
        }
        
        return PageInfo.of(articleDao.findByAuthor(user.getId(), typeId));
    }
    
    @Override
    public PageInfo<Article> searchTitle(String key, int nowPage, int pageSize) {
        PageHelper.startPage(nowPage, pageSize);
        
        if (null == key) {
            return new PageInfo(Collections.EMPTY_LIST);
        }
        return new PageInfo(articleDao.searchByTitleKey(key));
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
