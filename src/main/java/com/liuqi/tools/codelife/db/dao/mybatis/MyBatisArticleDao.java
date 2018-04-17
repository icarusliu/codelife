package com.liuqi.tools.codelife.db.dao.mybatis;

import com.liuqi.tools.codelife.db.dao.ArticleDao;
import com.liuqi.tools.codelife.db.dao.mybatis.mapper.ArticleMapper;
import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.UserArticleStatInfo;
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
    public List<Article> findForExplorerOrderByRecommended(Integer forumId) {
        return Optional.ofNullable(articleMapper.findForExplorerOrderByRecommended(forumId)).orElse(Collections.EMPTY_LIST);
    }
    
    @Override
    public List<Article> findForExplorerOrderByFixTop(Integer authorId, Integer typeId) {
        return Optional.ofNullable(articleMapper.findForExplorerOrderByFixTop(authorId, typeId))
                .orElse(Collections.EMPTY_LIST);
    }
    
    @Override
    public List<Article> findForManager(Integer authorId, Integer typeId) {
        return Optional.ofNullable(articleMapper.findForManager(authorId, typeId)).orElse(Collections.EMPTY_LIST);
    }
    
    @Override
    public List<Article> findByTopicForExplorer(Integer topicId) {
        if (null == topicId) {
            logger.error("TopicId cannot be null!");
            return Collections.EMPTY_LIST;
        }
        
        return Optional.ofNullable(articleMapper.findByTopicForExplorer(topicId)).orElse(Collections.EMPTY_LIST);
    }
    
    @Override
    public List<Article> findByTopicForManager(Integer topicId) {
        if (null == topicId) {
            logger.error("TopicId cannot be null!");
            return Collections.EMPTY_LIST;
        }
        
        return Optional.ofNullable(articleMapper.findByTopicForManager(topicId)).orElse(Collections.EMPTY_LIST);
    }
    
    @Override
    public List<Article> search(String key) {
        if (null == key) {
            key = "";
        }
        
        return Optional.ofNullable(articleMapper.search(key)).orElse(Collections.EMPTY_LIST);
    }
    
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
    public Integer save(Article article) {
        return articleMapper.save(article);
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
    
    @Override
    public void updateFixTop(Integer id, boolean fixTop) {
        articleMapper.updateFixTop(id, fixTop);
    }
    
    @Override
    public void updateRecommended(Integer id, boolean b) {
        articleMapper.updateRecommended(id, b);
    }
    
    @Override
    public UserArticleStatInfo getStatisticInfoByAuthor(int authorId) {
        return articleMapper.getStatisticInfoByAuthor(authorId);
    }
}
