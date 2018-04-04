package com.liuqi.tools.codelife.db.dao.mybatis;

import com.liuqi.tools.codelife.db.dao.TopicDao;
import com.liuqi.tools.codelife.db.dao.mybatis.mapper.TopicMapper;
import com.liuqi.tools.codelife.entity.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

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
    public Collection<Topic> findAll() {
        return topicMapper.findAll();
    }
    
    @Override
    public Collection<Topic> findByUser(Integer id) {
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
    public void insert(String title, String introduction, String img) {
        topicMapper.insert(title, introduction, img);
    }
    
    @Override
    public void update(Integer id, String title, String introduction, String img) {
        topicMapper.update(id, title, introduction, img);
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
}
