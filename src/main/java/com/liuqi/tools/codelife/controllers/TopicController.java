package com.liuqi.tools.codelife.controllers;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.util.FileUtils;
import com.liuqi.tools.codelife.util.MapBuilder;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.OutputStream;
import java.util.*;

/**
 * 专题控制器
 * 所有用户均可访问
 *
 * @Author: LiuQI
 * @Created: 2018/4/2 18:14
 * @Version: V1.0
 **/
@Controller
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Value("${app.file.topic.savePath}")
    private String saveFilePath;
    
    /**
     * 专题主页
     *
     * @return
     */
    @RequestMapping("/explorer")
    public ModelAndView topic() {
        //获取用户订阅专题及其更新的文章
        User loginUser = authenticationService.getLoginUser();
        Collection<Topic> myTopics;
        if (null != loginUser) {
            myTopics = topicService.getUserTopics(loginUser.getId());
        } else {
            myTopics = Collections.EMPTY_LIST;
        }
        if (0 != myTopics.size()) {
            myTopics.forEach(topic -> topic.setArticles(topicService.getTopicArticles(topic.getId())));
        }
        
        //获取所有的未订阅并且类型是开放的专题，私有的专题不能直接展现
        List<Topic> topics = topicService.findUserNotSubscribed(loginUser, 1, 20).getList();
        
        return ModelAndViewBuilder.of("topic")
                .setData("myTopics", myTopics)
                .setData("topics", topics)
                .build();
    }
    
    /**
     * 获取REST模式下专题浏览页面需要的数据
     */
    @PostMapping("/getExplorerData")
    @ResponseBody
    public Map<String, Object> getExplorerData() {
        //获取用户订阅专题及其更新的文章
        User loginUser = authenticationService.getLoginUser();
        Collection<Topic> myTopics;
        if (null != loginUser) {
            myTopics = topicService.getUserTopics(loginUser.getId());
        } else {
            myTopics = Collections.EMPTY_LIST;
        }
        if (0 != myTopics.size()) {
            myTopics.forEach(topic -> topic.setArticles(topicService.getTopicArticles(topic.getId())));
        }
    
        //获取所有的未订阅并且类型是开放的专题，私有的专题不能直接展现
        List<Topic> topics = topicService.findUserNotSubscribed(loginUser, 1, 20).getList();
        return MapBuilder.of()
                .put("myTopics", myTopics)
                .put("topics", topics)
                .build();
    }
    
    /**
     * 获取首页需要展示的专题数据
     *
     * @return
     */
    @PostMapping("/getForIndex")
    @ResponseBody
    public Map<String, Object> getForIndex() {
        //获取用户订阅专题及其更新的文章
        User loginUser = authenticationService.getLoginUser();
        Collection<Topic> myTopics;
        if (null != loginUser) {
            myTopics = topicService.getUserTopics(loginUser.getId());
        } else {
            myTopics = Collections.EMPTY_LIST;
        }
        if (0 != myTopics.size()) {
            myTopics.forEach(topic -> topic.setArticles(topicService.getTopicArticles(topic.getId())));
        }
    
        //获取所有的未订阅并且类型是开放的专题，私有的专题不能直接展现
        List<Topic> topics = topicService.findUserNotSubscribed(loginUser, 1, 20).getList();
    
        return MapBuilder.of().put("myTopic", myTopics)
                .put("topics", topics)
                .build();
    }
    
    /**
     * 获取专题下的文章
     *
     * @param topicId
     * @return
     */
    @RequestMapping("/getTopicArticles")
    @ResponseBody
    public Collection<Article> getTopicArticles(@RequestParam("topicId") Integer topicId) {
        return topicService.getTopicArticles(topicId);
    }
    
    /**
     * 订阅专题
     *
     * @param topicId
     * @throws RestException 如果专题编号不存在或者用户未登录时抛出异常
     */
    @RequestMapping("/subscribe")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public void subscribe(@RequestParam("id")Integer topicId) throws RestException {
        User loginUser = authenticationService.getLoginUser();
        
        //检查Topic是否存在
        Topic topic = topicService.findById(topicId);
        if (null == topic) {
            logger.error("Topic does not exist, id: " + topicId);
            throw ExceptionTool.getException(ErrorCodes.TOPIC_NOT_EXISTS, topicId);
        }
        
        //调用TopicService进行订阅
        topicService.subscribeTopic(loginUser.getId(), topicId);
    }
    
    @PostMapping("/unSubscribe")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public void unSubscribe(@RequestParam("id") Integer topicId) throws RestException {
        User loginUser = authenticationService.getLoginUser();
        
        topicService.unSubscribeTopic(loginUser.getId(), topicId);
    }
    
    @RequestMapping("/getImg")
    @ResponseBody
    public void getImg(@RequestParam("fileName") String fileName, OutputStream outputStream) throws RestException {
        FileUtils.outputFileContent(saveFilePath, fileName, outputStream);
    }
    
    /**
     * 打开专题详细页面
     * @param topicId
     * @return
     */
    @GetMapping("/detail")
    public ModelAndView detail(@RequestParam(value = "id", required = false) Integer topicId) {
        return ModelAndViewBuilder.of("topicDetail")
                .setDatasFromMap(getMyTopicsData(topicId))
                .build();
    }
    
    /**
     * 返回REST方式下专题详细页面的数据
     *
     * @param topicId
     * @return
     */
    @PostMapping("/getMyTopics")
    @ResponseBody
    public Map<String, Object> getMyTopics(@RequestParam(value = "id", required = false) Integer topicId) {
        return getMyTopicsData(topicId);
    }
    
    /**
     * 获取个人专题页面需要的数据
     *
     * @param topicId
     * @return
     */
    private Map<String, Object> getMyTopicsData(Integer topicId) {
        //获取用户订阅专题
        User loginUser = authenticationService.getLoginUser();
        Collection<Topic> myTopics;
        if (null != loginUser) {
            myTopics = topicService.getUserTopics(loginUser.getId());
        } else {
            myTopics = Collections.EMPTY_LIST;
        }
    
        //如果专题编号未传入，且用户已经登录，则获取用户订阅的第一个专题进行展示
        Topic topic = null;
        if (null == topicId && null != loginUser) {
            List<Topic> topics = topicService.getUserTopics(loginUser.getId());
            if (null != topics && 0 < topics.size()) {
                topic = topics.get(0);
                topicId = topic.getId();
            }
        } else {
            topic = topicService.findById(topicId);
        }
    
        //获取专题订阅用户清单
        List<User> userList;
        if (null != topicId) {
            userList = topicService.getTopicUsers(topicId);
        } else {
            userList = Collections.EMPTY_LIST;
        }
    
        //获取专题文章清单
        List<Article> articleList;
        if (null != topicId) {
            articleList = topicService.getTopicArticles(topicId);
        } else {
            articleList = Collections.EMPTY_LIST;
        }
    
        return MapBuilder.of()
                .put("topic", topic)
                .put("articles", articleList)
                .put("myTopics", myTopics)
                .put("userList", userList)
                .build();
    }
    
    /**
     * 根据关键字搜索专题
     * 关键字将会命中专题的标题、介绍字段
     *
     * @param key
     * @return
     */
    @GetMapping("/search")
    public ModelAndView search(@RequestParam("key") String key,
                               @RequestParam(value = "nowPage", required = false) Integer nowPage,
                               @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        nowPage = null == nowPage ? 1 : nowPage;
        pageSize = null == pageSize ? 20 : pageSize;
        
        PageInfo<Topic> topics = topicService.search(key, nowPage, pageSize);
        
        return ModelAndViewBuilder.of("topicSearch")
                .setData("topics", topics.getList())
                .setData("nowPage", nowPage)
                .setData("pages", topics.getPages())
                .setData("key", key)
                .build();
    }
    
    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);
}
