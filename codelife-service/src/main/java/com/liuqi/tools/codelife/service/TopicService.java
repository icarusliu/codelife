package com.liuqi.tools.codelife.service;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.entity.*;
import com.liuqi.tools.codelife.exceptions.RestException;

import java.util.List;

/**
 * 专题服务接口
 * @author qi.liu
 */
public interface TopicService {
    /**
     * 查找所有专题
     *
     * @param nowPage
     * @param pageSize
     * @return
     */
    PageInfo findAll(int nowPage, int pageSize);
    
    /**
     * 获取用户未订阅的所有开放的专题
     *
     * @param user
     * @param nowPage
     * @param pageSize
     * @return
     */
    PageInfo<Topic> findUserNotSubscribed(User user, int nowPage, int pageSize);
    
    /**
     * 获取专题下的所有文章清单
     *
     * @param id
     * @return
     */
    List<Article> getTopicArticles(Integer id);
    
    /**
     * 获取用户所订阅的专题
     *
     * @param id
     * @return
     */
    List<Topic> getUserTopics(Integer id);
    
    /**
     * 通过标题获取专题
     *
     * @param title
     * @return
     */
    Topic findByTitle(String title);

    /**
     * 通过编号查找专题
     * @param id
     * @return
     */
    Topic findById(Integer id);
    
    /**
     * 新增专题
     * @param title
     * @param introduction
     * @param img
     * @param topicType
     * @param creator
     * @throws RestException
     */
    void addTopic(String title, String introduction, String img, TopicType topicType, User creator) throws RestException;
    
    /**
     * 更新专题
     *
     * @param id
     * @param title
     * @param introduction
     * @param img
     * @param type
     * @param admin
     * @throws RestException
     */
    void updateTopic(Integer id, String title, String introduction, String img, TopicType type, Integer admin) throws RestException;
    
    /**
     * 批量配置专题中的文章
     *
     * @param id
     * @param articles 文件的编号清单
     * @throws RestException
     */
    void addTopicArticls(Integer id, List<Integer> articles) throws RestException;
    
    /**
     * 某个用户订阅专题
     *
     * @param userId
     * @param topicId
     * @throws RestException
     */
    void subscribeTopic(Integer userId, Integer topicId) throws RestException;
    
    /**
     * 某个用户取消订阅专题
     *
     * @param userId
     * @param topicId
     * @throws RestException
     */
    void unSubscribeTopic(Integer userId, Integer topicId) throws RestException;
    
    /**
     * 删除专题
     * 删除专题后会将对应用户的订阅关系以及专题中包含的文章两层关系均进行删除
     *
     * @param id
     * @throws RestException
     */
    void deleteTopic(Integer id) throws RestException;
    
    /**
     * 删除专题中的某个文章
     *
     * @param id
     * @param articleId
     */
    void deleteTopicArticle(Integer id, Integer articleId);
    
    /**
     * 更新专题状态
     *
     * @param id
     * @param status
     */
    void updateStatus(Integer id, TopicStatus status);
    
    /**
     * 获取管理员为指定用户的专题清单
     *
     * @param loginUser
     * @return
     */
    List findByAdmin(User loginUser);
    
    /**
     * 获取专题订阅用户列表
     *
     * @param topicId
     * @return
     */
    List<User> getTopicUsers(Integer topicId);
    
    /**
     * 根据关键字搜索专题
     *
     * @param key
     * @param nowPage
     * @param pageSize
     * @return
     */
    PageInfo<Topic> search(String key, int nowPage, int pageSize);

    /**
     * 通过文章查找该文章所属的标题清单
     * @param articleId
     * @return
     */
    List<Topic> findByArticle(Integer articleId);

    /**
     * 根据文章编号删除所有该文章专题关系
     *
     * @param articleId 文章编号
     */
    void deleteTopicArticle(Integer articleId);
}
