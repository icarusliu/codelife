package com.liuqi.tools.codelife.db.dao.mybatis.mapper;

import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.entity.TopicStatus;
import com.liuqi.tools.codelife.entity.TopicType;
import com.liuqi.tools.codelife.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @Author: LiuQI
 * @Created: 2018/4/3 11:36
 * @Version: V1.0
 **/
@Mapper
public interface TopicMapper {
    Collection<Topic> findAll();
    
    Collection<Topic> findByUser(@Param("id") Integer id);
    
    Topic findByTitle(@Param("title") String title);
    
    Topic findById(@Param("id") Integer id);
    
    void insert(Topic topic);
    
    void update(Topic topic);
    
    void addTopicArticles(@Param("id") Integer id, @Param("articles") List<Integer> articles);
    
    void subscribeTopic(@Param("userId") Integer userId, @Param("topicId") Integer topicId);
    
    void unSubscribeTopic(@Param("userId") Integer userId, @Param("topicId") Integer topicId);
    
    void delete(@Param("id") Integer id);
    
    /**
     * 删除Topic下的指定文章
      * @param id
     * @param articleId
     */
    void deleteTopicArticle(@Param("id") Integer id, @Param("articleId") Integer articleId);
    
    /**
     * 清空专题的订阅关系
     * 
     * @param id
     */
    void clearTopicSubscribers(@Param("id") Integer id);
    
    /**
     * 清空专题的文章
     * @param id
     */
    void clearTopicArticles(@Param("id")Integer id);
    
    /**
     * 获取用户未订阅的所有开放的专题
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
    void updateStatus(@Param("id") Integer id, @Param("status")TopicStatus status);
    
    /**
     * 获取管理员为指定用户编号的专题清单
     *
     * @param id 用户编号
     * @return
     */
    Collection<Topic> findByAdmin(@Param("userId") int id);
    
    /**
     * 根据关键字搜索专题
     *
     * @param key
     * @return
     */
    Collection<Topic> search(@Param("key") String key);
}
