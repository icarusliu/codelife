package com.liuqi.tools.codelife.service;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.exceptions.RestException;

import java.util.Collection;
import java.util.List;

/**
 * 专题服务接口
 */
public interface TopicService {
    /**
     * 查找所有专题
     *
     * @return
     */
    Collection<Topic> findAll();
    
    /**
     * 获取专题下的所有文章清单
     *
     * @param id
     * @return
     */
    Collection<Article> getTopicArticles(Integer id);
    
    /**
     * 获取用户所订阅的专题
     *
     * @param id
     * @return
     */
    Collection<Topic> getUserTopics(Integer id);
    
    /**
     * 通过标题获取专题
     *
     * @param title
     * @return
     */
    Topic findByTitle(String title);
    
    Topic findById(Integer id);
    
    /**
     * 新增专题
     *
     * @param title
     * @param introduction
     * @param img
     */
    void addTopic(String title, String introduction, String img) throws RestException;
    
    /**
     * 更新专题
     *
     * @param id
     * @param title
     * @param introduction
     * @param img
     */
    void updateTopic(Integer id, String title, String introduction, String img) throws RestException;
    
    /**
     * 批量配置专题中的文章
     *
     * @param id
     * @param articles 文件的编号清单
     */
    void addTopicArticls(Integer id, List<Integer> articles) throws RestException;
    
    /**
     * 某个用户订阅专题
     *
     * @param userId
     * @param topicId
     */
    void subscribeTopic(Integer userId, Integer topicId) throws RestException;
    
    /**
     * 某个用户取消订阅专题
     *
     * @param userId
     * @param topicId
     */
    void unSubscribeTopic(Integer userId, Integer topicId) throws RestException;
    
    /**
     * 删除专题
     * 删除专题后会将对应用户的订阅关系以及专题中包含的文章两层关系均进行删除
     *
     * @param id
     */
    void deleteTopic(Integer id) throws RestException;
}
