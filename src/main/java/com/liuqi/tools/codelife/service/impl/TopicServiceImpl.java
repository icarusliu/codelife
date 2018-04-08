package com.liuqi.tools.codelife.service.impl;

import com.liuqi.tools.codelife.db.dao.ArticleDao;
import com.liuqi.tools.codelife.db.dao.TopicDao;
import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.service.UserService;
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
 * 专题服务实现类
 *
 * @Author: LiuQI
 * @Created: 2018/4/3 10:46
 * @Version: V1.0
 **/
@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicDao topicDao;
    
    @Autowired
    private ArticleDao articleDao;
    
    @Autowired
    private UserService userService;
    
    @Value("${app.restrict.topic.maxTitleLength}")
    private String maxTitleLength;
    
    @Value("${app.restrict.topic.maxIntroductionLength}")
    private String maxIntroductionLength;
    
    @Value("${app.restrict.topic.maxImgLength}")
    private String maxImgLength;
    
    /**
     * 查找所有专题清单
     *
     * @return 如果没有专题返回的将是空的List
     */
    @Override
    public Collection<Topic> findAll() {
        return Optional.ofNullable(topicDao.findAll()).orElse(Collections.EMPTY_LIST);
    }
    
    /**
     * 查找专题下的所有文章
     * @param id
     * @return 如果没有文章将返回空的List
     */
    @Override
    public Collection<Article> getTopicArticles(Integer id) {
        return Optional.ofNullable(articleDao.findByTopic(id)).orElse(Collections.EMPTY_LIST);
    }
    
    /**
     * 查找用户订阅的专题清单
     *
     * @param id 用户编号
     * @return 如果未订阅返回空的List
     */
    @Override
    public Collection<Topic> getUserTopics(Integer id) {
        return Optional.ofNullable(topicDao.findByUser(id)).orElse(Collections.EMPTY_LIST);
    }
    
    /**
     * 通过标题查找专题
     * 如果未找着将返回Null
     * @param title
     * @return 未找着时舞台Null
     */
    @Override
    public Topic findByTitle(String title) {
        return topicDao.findByTitle(title);
    }
    
    @Override
    public Topic findById(Integer id) {
        return topicDao.findById(id);
    }
    
    /**
     * 新增专题

     * @param title
     * @param introduction
     * @param img
     */
    @Override
    public void addTopic(String title, String introduction, String img) throws RestException {
        checkTopicProps(title, introduction, img);
        
        topicDao.insert(title, introduction, img);
    }
    
    @Override
    public void updateTopic(Integer id, String title, String introduction, String img) throws RestException {
        checkTopicProps(title, introduction, img);
        
        if (null == id) {
            logger.error("ID cannot be null when updating it!");
            throw new RestException("编号不能为空！");
        }
        
        //检测ID是否存在
        Topic topic = findById(id);
        if (null != topic) {
            logger.error("Topic with the special id does not exist, id: " + id);
            throw new RestException("指定编号的专题不存在！");
        }
        
        topicDao.update(id, title, introduction, img);
    }
    
    /**
     * 新增与修改专题时属性校验
     * 需要满足以下要求：
     * 专题名称不能重复；
     * 专题不能为空；
     * 各字段长度不能超限制；
     * 专题描述不能为空；专题封面可以为空。
     *
     * @param title
     * @param introduction
     * @param img
     */
    private void checkTopicProps(String title, String introduction, String img) throws RestException {
        //为空判断
        if (null == title || title.trim().equals("")) {
            logger.error("Topic title cannot be null or empty!");
            throw new RestException("专题标题不能为空！");
        }
        
        if (null == introduction || "".equals(introduction.trim())) {
            logger.error("Topic introduction cannot be null or empty!");
            throw new RestException("专题介绍不能为空！");
        }
        
        //检查长度是否超过限制
        if (title.length() > Integer.valueOf(maxTitleLength)) {
            logger.error("Topic title is too long, title: {}, maxLength: {}!", title, maxTitleLength);
            throw new RestException("标题长度不能超过" + maxTitleLength);
        }
        
        if (introduction.length() > Integer.valueOf(maxIntroductionLength)) {
            logger.error("Topic introduction is too long, introduction: {}, maxLength: {}!"
                    , introduction, maxIntroductionLength);
            throw new RestException("介绍长度不能超过" + maxIntroductionLength);
        }
        
        if (null != img && img.length() > Integer.valueOf(maxImgLength)) {
            logger.error("Topic img is too long, img: {}, maxLength: {}!", img, maxImgLength);
            throw new RestException("封面长度不能超过" + maxImgLength);
        }
        
        //检测同名的是否存在
        Topic topic = findByTitle(title);
        if (null != topic) {
            logger.error("Topic with the same title already exists!");
            throw new RestException("同名的专题已经存在，请不要重复添加！");
        }
    }
    
    @Override
    public void addTopicArticls(Integer id, List<Integer> articles) throws RestException {
        checkTopicId(id);
        
        //如果已经添加过的不再重复添加
        Collection<Integer> articles1 = getTopicArticles(id).stream()
                .map(item -> item.getId()).collect(Collectors.toList());
        List<Integer> notAdded = articles.stream().filter(item -> !articles1.contains(item))
                .collect(Collectors.toList());
        if (0 != notAdded.size()) {
            topicDao.addTopicArticles(id, notAdded);
        }
    }
    
    @Override
    public void subscribeTopic(Integer userId, Integer topicId) throws RestException {
        checkTopicId(topicId);
        
        if (null == userId) {
            logger.error("User id cannot be null!");
            throw new RestException("用户编号不能为空！");
        }
        
        //检查用户编号是否存在
        userService.findById(userId);
        
        //检查是否已经有过订阅
        Collection<Topic> myTopics = topicDao.findByUser(userId);
        if (null != myTopics) {
            for (Topic topic: myTopics) {
                if (topic.getId() == topicId) {
                    logger.warn("Topic already subscribed, topicId: " + topicId);
                    return;
                }
            }
        }
        
        topicDao.subscribeTopic(userId, topicId);
    }
    
    @Override
    public void unSubscribeTopic(Integer userId, Integer topicId) throws RestException {
        checkTopicId(topicId);
    
        if (null == userId) {
            logger.error("User id cannot be null!");
            throw new RestException("用户编号不能为空！");
        }
    
        userService.findById(userId);
    
        topicDao.unSubscribeTopic(userId, topicId);
    }
    
    @Override
    public void deleteTopic(Integer id) throws RestException {
        checkTopicId(id);
     
        //先删除用户订阅关系再删除专题下的所有文章，最后删除专题对象
        topicDao.clearTopicSubscribers(id);
        topicDao.clearTopicArticles(id);
        
        topicDao.delete(id);
    }
    
    @Override
    public void deleteTopicArticle(Integer id, Integer articleId) {
        topicDao.deleteTopicArticle(id, articleId);
    }
    
    private void checkTopicId(Integer id) throws RestException {
        if (null == id ) {
            logger.error("ID cannot be null!");
            throw new RestException("专题编号不能为空！");
        }
    
        if (null == findById(id)) {
            logger.error("Topic with the special id does not exist, id: " + id);
            throw new RestException("指定编号的专题不存在！");
        }
    }
    
    private static final Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);
}
