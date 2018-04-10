package com.liuqi.tools.codelife.db.dao.mybatis.mapper;

import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.entity.TopicType;
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
}
