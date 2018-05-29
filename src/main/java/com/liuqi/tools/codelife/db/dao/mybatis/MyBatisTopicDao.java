package com.liuqi.tools.codelife.db.dao.mybatis;

import com.liuqi.tools.codelife.db.dao.TopicDao;
import com.liuqi.tools.codelife.db.dao.mybatis.mapper.TopicMapper;
import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.entity.TopicStatus;
import com.liuqi.tools.codelife.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * MyBatis专题数据库操作类
 *
 * @Author: LiuQIw
 * @Created: 2018/4/3 11:35
 * @Version: V1.0
 **/
@Repository
public class MyBatisTopicDao implements TopicDao {
    @Autowired
    private TopicMapper topicMapper;
    
    @Override
    public List<Topic> findAll() {
        return topicMapper.findAll();
    }
    
    @Override
    public List<Topic> findByUser(Integer id) {
        return topicMapper.findByUser(id);
    }
    
    @Override
    public Topic findByTitle(String title) {
        return topicMapper.findByTitle(title);
    }
    
    @Override
    public Topic findById(Integer id) {
        return topicMapper.findById(id);
    }
    
    @Override
    public void insert(Topic topic) {
        topicMapper.insert(topic);
    }
    
    @Override
    public void update(Topic topic) {
        topicMapper.update(topic);
    }
    
    @Override
    public void addTopicArticles(Integer id, List<Integer> articles) {
        topicMapper.addTopicArticles(id, articles);
    }
    
    @Override
    public void subscribeTopic(Integer userId, Integer topicId) {
        topicMapper.subscribeTopic(userId, topicId);
    }
    
    @Override
    public void unSubscribeTopic(Integer userId, Integer topicId) {
        topicMapper.unSubscribeTopic(userId, topicId);
    }
    
    @Override
    public void delete(Integer id) {
        topicMapper.delete(id);
    }
    
    @Override
    public void clearTopicArticles(Integer id) {
        topicMapper.clearTopicArticles(id);
    }
    
    @Override
    public void clearTopicSubscribers(Integer id) {
        topicMapper.clearTopicSubscribers(id);
    }
    
    @Override
    public void deleteTopicArticle(Integer id, Integer articleId) {
        topicMapper.deleteTopicArticle(id, articleId);
    }
    
    @Override
    public List<Topic> findUserNotSubscribed(User user) {
        return topicMapper.findUserNotSubscribed(user);
    }
    
    @Override
    public void updateStatus(Integer id, TopicStatus status) {
        topicMapper.updateStatus(id, status);
    }
    
    @Override
    public List<Topic> findByAdmin(User loginUser) {
        return Optional.ofNullable(topicMapper.findByAdmin(loginUser.getId())).orElse(Collections.EMPTY_LIST);
    }
    
    @Override
    public List<Topic> search(String key) {
        return Optional.ofNullable(topicMapper.search(key)).orElse(Collections.EMPTY_LIST);
    }
}
