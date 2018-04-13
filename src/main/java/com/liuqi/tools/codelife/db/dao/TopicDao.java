package com.liuqi.tools.codelife.db.dao;

import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.entity.TopicStatus;
import com.liuqi.tools.codelife.entity.TopicType;
import com.liuqi.tools.codelife.entity.User;

import java.util.Collection;
import java.util.List;

/**
 * Topic数据库操作类
 *
 */
public interface TopicDao {
    /**
     * 查找所有专题清单
     *
     * @return
     */
    Collection<Topic> findAll();
    
    /**
     * 查找用户订阅的所有专题
     *
     * @param id 用户编号
     * @return
     */
    Collection<Topic> findByUser(Integer id);
    
    /**
     * 通过标题查找Topic
     *
     * @param title
     * @return
     */
    Topic findByTitle(String title);
    
    /**
     * 通过编号查找专题
     *
     * @param id
     * @return
     */
    Topic findById(Integer id);
    
    /**
     * 新增专题
     * @param topic
     */
    void insert(Topic topic);
    
    /**
     * 更新专题
     *
     * @param topic
     */
    void update(Topic topic);
    
    /**
     * 批量在专题下新增文章
     * @param id
     * @param articles
     */
    void addTopicArticles(Integer id, List<Integer> articles);
    
    /**
     * 用户订阅专题
     *
     * @param userId
     * @param topicId
     */
    void subscribeTopic(Integer userId, Integer topicId);
    
    /**
     * 用户取消订阅专题
     *
     * @param userId
     * @param topicId
     */
    void unSubscribeTopic(Integer userId, Integer topicId);
    
    /**
     * 删除专题
     *
     * @param id
     */
    void delete(Integer id);
    
    /**
     * 清空专题下的文章
     *
     * @param id
     */
    void clearTopicArticles(Integer id);
    
    /**
     * 清空专题的订阅关系
     *
     * @param id
     */
    void clearTopicSubscribers(Integer id);
    
    /**
     * 删除专题下的指定文章
     *
     * @param id
     * @param articleId
     */
    void deleteTopicArticle(Integer id, Integer articleId);
    
    /**
     * 获取用户未订阅的所有开放专题
     *
     * @param user
     * @return
     */
    Collection<Topic> findUserNotSubscribed(User user);
    
    /**
     * 更新专题状态
     *
      * @param id
     * @param status
     */
    void updateStatus(Integer id, TopicStatus status);
    
    /**
     * 获取管理员为指定用户的所有专题
     *
     * @param loginUser
     * @return
     */
    Collection<Topic> findByAdmin(User loginUser);
    
    /**
     * 根据关键字搜索专题
     *
     * @param key
     * @return
     */
    Collection<Topic> search(String key);
}
