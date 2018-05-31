package com.liuqi.tools.codelife.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.db.dao.ArticleDao;
import com.liuqi.tools.codelife.db.dao.TopicDao;
import com.liuqi.tools.codelife.db.dao.UserDao;
import com.liuqi.tools.codelife.entity.*;
import com.liuqi.tools.codelife.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.RoleService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

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
    private UserDao userDao;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
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
    public PageInfo<Topic> findAll(int nowPage, int pageSize) {
        PageHelper.startPage(nowPage, pageSize);
        return PageInfo.of(Optional.ofNullable(topicDao.findAll()).orElse(Collections.EMPTY_LIST));
    }
    
    /**
     * 获取指定用户未订阅的所有开放的专题清单
     *
     * @param user
     * @return 如果无专题将返回空的List
     */
    @Override
    public PageInfo<Topic> findUserNotSubscribed(User user, int nowPage, int pageSize) {
        PageHelper.startPage(nowPage, pageSize);
        return PageInfo.of(Optional.ofNullable(topicDao.findUserNotSubscribed(user)).orElse(Collections.EMPTY_LIST));
    }
    
    /**
     * 查找专题下的所有文章
     * @param id
     * @return 如果没有文章将返回空的List
     */
    @Override
    public List<Article> getTopicArticles(Integer id) {
        return Optional.ofNullable(articleDao.findByTopicForExplorer(id)).orElse(Collections.EMPTY_LIST);
    }
    
    /**
     * 查找用户订阅的专题清单
     *
     * @param id 用户编号
     * @return 如果未订阅返回空的List
     */
    @Override
    public List<Topic> getUserTopics(Integer id) {
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
     * @param topicType
     */
    @Override
    public void addTopic(String title, String introduction, String img, TopicType topicType, User creator) throws RestException {
        checkTopicProps(title, introduction, img);
    
        //检测同名的是否存在
        Topic findTopic = findByTitle(title);
        if (null != findTopic) {
            logger.error("Topic with the same title already exists!");
            throw ExceptionTool.getException(ErrorCodes.TOPIC_NAME_EXISTS);
        }
        
        Topic topic = new Topic();
        topic.setCreator(creator);
        topic.setAdmin(creator);
        topic.setTitle(title);
        topic.setIntroduction(introduction);
        topic.setImg(img);
        topic.setType(topicType);
        topic.setStatus(TopicStatus.WAIT_APPROVE);
        
        topicDao.insert(topic);
    }
    
    @Override
    public void updateTopic(Integer id, String title, String introduction, String img,
                            TopicType type, Integer admin) throws RestException {
        checkTopicProps(title, introduction, img);
        
        if (null == id) {
            logger.error("ID cannot be null when updating it!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "编号");
        }
        
        //检测ID是否存在
        Topic topic = findById(id);
        if (null == topic) {
            logger.error("Topic with the special id does not exist, id: " + id);
            throw ExceptionTool.getException(ErrorCodes.TOPIC_NOT_EXISTS, id);
        }
        
        //如果管理员账号有变更，需要给新的专题管理员添加专题管理员角色；
        if (null != admin && topic.getAdmin().getId() != admin) {
            User adminUser = userService.findById(admin);
            Collection<Role> roles = roleService.findAllRoles();
            Role findRole = null;
            
            for (Role role: roles) {
                if ("TOPIC_ADMIN".equals(role.getName())) {
                    findRole = role;
                    
                    break;
                }
            }
            
            if (null == findRole) {
                logger.error("Role with name TOPIC_ADMIN does not exist!");
                throw ExceptionTool.getException(ErrorCodes.ROLE_NOT_EXISTS, "TOPIC_ADMIN");
            }
            
            userService.addRole(adminUser, findRole);
        }
        
        topic.setTitle(title);
        topic.setIntroduction(introduction);
        topic.setImg(img);
        topic.setType(type);
        if (null != admin) {
            User adminUser = userService.findById(admin);
            topic.setAdmin(adminUser);
        }
        
        topicDao.update(topic);
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
        if (StringUtils.isEmpty(title)) {
            logger.error("Topic title cannot be null or empty!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "专题名称");
        }

        if (StringUtils.isEmpty(introduction)) {
            logger.error("Topic introduction cannot be null or empty!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "专题介绍");
        }

        //检查长度是否超过限制
        if (title.length() > Integer.valueOf(maxTitleLength)) {
            logger.error("Topic title is too long, title: {}, maxLength: {}!", title, maxTitleLength);
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_TOO_LONG, "专题名称", maxTitleLength);
        }

        if (introduction.length() > Integer.valueOf(maxIntroductionLength)) {
            logger.error("Topic introduction is too long, introduction: {}, maxLength: {}!"
                    , introduction, maxIntroductionLength);
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_TOO_LONG, "专题介绍", maxIntroductionLength);
        }

        if (null != img && img.length() > Integer.valueOf(maxImgLength)) {
            logger.error("Topic img is too long, img: {}, maxLength: {}!", img, maxImgLength);
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_TOO_LONG, "专题封面", maxIntroductionLength);
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
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "用户编号");
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
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "用户编号");
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
    
    @Override
    public void updateStatus(Integer id, TopicStatus status) {
        if (null == id || null == status) {
            logger.warn("ID or status cannot be null!");
            return;
        }
        topicDao.updateStatus(id, status);
    }
    
    @Override
    public List<Topic> findByAdmin(User loginUser) {
        if (null == loginUser) {
            logger.error("User cannot be null!");
            return Collections.EMPTY_LIST;
        }
        
        return topicDao.findByAdmin(loginUser.getId());
    }
    
    @Override
    public List<User> getTopicUsers(Integer topicId) {
        return userDao.getTopicUsers(topicId);
    }
    
    @Override
    public PageInfo<Topic> search(String key, int nowPage, int pageSize) {
        if (null == key) {
            logger.warn("Key cannot be null!");
            return new PageInfo(Collections.EMPTY_LIST);
        }
    
        PageHelper.startPage(nowPage, pageSize);
    
        return PageInfo.of(topicDao.search(key));
    }
    
    private void checkTopicId(Integer id) throws RestException {
        if (null == id ) {
            logger.error("ID cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "专题编号");
        }
    
        if (null == findById(id)) {
            logger.error("Topic with the special id does not exist, id: " + id);
            throw ExceptionTool.getException(ErrorCodes.TOPIC_NOT_EXISTS, id);
        }
    }
    
    private static final Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);
}
