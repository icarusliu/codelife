package com.liuqi.tools.codelife.db.dao.mybatis;

import com.liuqi.tools.codelife.db.dao.ArticleDao;
import com.liuqi.tools.codelife.db.dao.mybatis.mapper.ArticleMapper;
import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.exceptions.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * MyBatis的文章操作实现类
 *
 * @Author: LiuQI
 * @Created: 2018/3/26 17:17
 * @Version: V1.0
 **/
@Repository
public class MyBatisArticleDao implements ArticleDao{
    private static final Logger logger = LoggerFactory.getLogger(MyBatisArticleDao.class);
    
    @Autowired
    private ArticleMapper articleMapper;
    
    @Override
    public Article findById(int id) throws RestException {
        Article article = articleMapper.findById(id);
        if (null == article) {
            logger.error("Article does not exist, id: {}!", id);
            throw new RestException("文章不存在，请确认是否已删除！");
        }
        
        return article;
    }
    
    @Override
    public List<Article> findByAuthor(int authorId, Integer typeId) {
        return Optional.ofNullable(articleMapper.findByAuthor(authorId, typeId)).orElse(new ArrayList<>());
    }
    
    @Override
    public List<Article> findAll() {
        return Optional.ofNullable(articleMapper.findAll()).orElse(new ArrayList<>());
    }
    
    @Override
    public Integer save(Article article) {
        return articleMapper.save(article);
    }
    
    @Override
    public List<Article> findByForum(Integer forumId) {
        return Optional.ofNullable(articleMapper.findByForum(forumId)).orElse(new ArrayList<>());
    }
    
    @Override
    public void addReadCount(int id) {
        articleMapper.addReadCount(id);
    }
    
    @Override
    public void deleteArticle(Integer id) {
        articleMapper.deleteArticle(id);
    }
    
    @Override
    public void updateArticle(Integer id, String title, Integer type, Integer forumId) {
        articleMapper.updateArticle(id, title, type, forumId);
    }
    
    /**
     * 按ReadCount及CreateDate排序返回前指定个数的文章
     *
     * @param i 需要返回的文章数
     * @return 获取的文章清单，如果无则返回空的List
     */
    @Override
    public List<Article> findTopArticles(int i) {
        return Optional.ofNullable(articleMapper.findTopArticles(i)).orElse(Collections.EMPTY_LIST);
    }
    
    /**
     * 为文章的点赞数增加一个值
     * 这个值可以是1或者-1，1时为点赞、-1时为取消点赞
     *
     * @param id 文章ID
     * @param i 变动的点赞数，可以是1或者是-1
     */
    @Override
    public void addPraiseCount(int id, int i) {
       articleMapper.addPraiseCount(id, i);
    }
    
    /**
     * 查找专题下的所有文章
     *
     * @param id
     * @return
     */
    @Override
    public List<Article> findByTopic(Integer id) {
        return articleMapper.findByTopic(id);
    }
    
    @Override
    public List<Article> searchByTitleKey(String key) {
        return Optional.ofNullable(articleMapper.searchByTitleKey(key)).orElse(Collections.EMPTY_LIST);
    }
    
    @Override
    public void updateFixTop(Integer id, boolean fixTop) {
        articleMapper.updateFixTop(id, fixTop);
    }
    
    @Override
    public void updateRecommended(Integer id, boolean b) {
        articleMapper.updateRecommended(id, b);
    }
}
