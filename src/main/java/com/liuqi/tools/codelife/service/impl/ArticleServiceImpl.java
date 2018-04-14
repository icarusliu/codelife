package com.liuqi.tools.codelife.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.db.dao.ArticleDao;
import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleService;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.UserService;
import com.liuqi.tools.codelife.util.ArticleBuilder;
import com.liuqi.tools.codelife.util.FileUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    
    @Value("${app.file.savePath}")
    private String contentFilePath;
    
    @Override
    public PageInfo<Article> findAll(int nowPage, int pageSize) {
        PageHelper.startPage(nowPage, pageSize);
        
        List<Article> articles = articleDao.findAll();
        
        articles.stream().map(article -> {
            try {
                article.setContent(FileUtils.getFileContent(article.getContentUrl(), contentFilePath));
            } catch (RestException e) {
                //对于获取内容失败的文章不返回
                logger.error("Get file content failed!", e);
            }
            return article;
        });
        
        return new PageInfo(articles);
    }
    
    @Override
    public Article findById(int id) throws RestException {
        Article article = articleDao.findById(id);
        article.setContent(FileUtils.getFileContent(article.getContentUrl(), contentFilePath));
        
        return article;
    }
    
    @Override
    public List<ArticleType> findAllTypes() {
        return articleDao.findAllTypes();
    }
    
    @Override
    public ArticleType findTypeById(int id) throws RestException {
        ArticleType type = articleDao.findTypeById(id);
        if (null == type) {
            logger.error("Type does not exist, id: {}!", id);
            throw new RestException("类型不存在，类型ID：" + id);
        }
        
        return type;
    }
    
    @Override
    public void saveArticle(String title, String content, int type) throws RestException {
        User user = authenticationService.getLoginUser();
        
        Article article = ArticleBuilder.of(title)
                .setType(findTypeById(type))
                .setContent(content, contentFilePath)
                .setAuthor(user)
                .build();
        articleDao.save(article);
    }
    
    @Override
    public PageInfo<Article> findByType(Integer id, int nowPage, Integer pageSize) {
        PageHelper.startPage(nowPage, pageSize);
    
        return new PageInfo(articleDao.findByType(id));
    }
    
    /**
     * 保存文章分类
     *
     * @param name
     * @throws RestException
     */
    @Override
    public void saveType(String name) throws RestException {
        //先检查同名的Type是否存在，如果存在则抛出异常
        ArticleType type = articleDao.findTypeByName(name);
        if (null != type) {
            logger.error("Article type with the same name exists already, name: " + name);
            throw new RestException("相同名称的分类已经存在，请确认！");
        }
        
        //不存在时增加
        type = new ArticleType();
        type.setName(name);
        type.setUserId(authenticationService.getLoginUser().getId());
        articleDao.saveType(type);
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
    }
    
    /**
     * 更新文章
     *
     * @param id
     * @param title
     * @param content
     * @param type
     */
    @Override
    public void updateArticle(Integer id, String title, String content, Integer type) throws RestException {
        Article article = articleDao.findById(id);
        
        //更新文件内容
        FileUtils.saveToFile(content, contentFilePath, article.getContentUrl());
        
        //更新数据库信息
        articleDao.updateArticle(id, title, type);
    }
    
    @Override
    public void saveType(Integer id, String name) {
        articleDao.renameType(id, name);
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
     * @return 如果未发表过文章时返回空的List
     */
    @Override
    public PageInfo<Article> findByAuthor(User user, int nowPage, int pageSize) throws RestException {
        PageHelper.startPage(nowPage, pageSize);
        if (null == user || 0 == user.getId()) {
            logger.error("User or the id of user is null!");
            throw new RestException("用户或用户编号为空！");
        }
        
        return PageInfo.of(articleDao.findByAuthor(user.getId()));
    }
    
    @Override
    public PageInfo<Article> searchTitle(String key, int nowPage, int pageSize) {
        PageHelper.startPage(nowPage, pageSize);
        
        if (null == key) {
            return new PageInfo(Collections.EMPTY_LIST);
        }
        return new PageInfo(articleDao.searchByTitleKey(key));
    }
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
}
