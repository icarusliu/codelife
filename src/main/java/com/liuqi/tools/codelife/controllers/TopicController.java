package com.liuqi.tools.codelife.controllers;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.util.FileUtils;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

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
    @RequestMapping
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
        
        //获取所有的未订阅专题
        Collection<Topic> topics = topicService.findAll();
        if (null != topics) {
            topics = topics.stream().filter(topic -> !myTopics.contains(topic))
                    .map(topic -> {
                        topic.setArticles(topicService.getTopicArticles(topic.getId()));
                        
                        return topic;
                    }).collect(Collectors.toList());
        }
        
        return ModelAndViewBuilder.of("topic")
                .setData("myTopics", myTopics)
                .setData("topics", topics)
                .build();
    }
    
    @RequestMapping("/getAll")
    @ResponseBody
    public Collection<Topic> getAll() {
        return topicService.findAll();
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
            throw new RestException("专题编号不存在！");
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
    public ModelAndView detail(@RequestParam("id") Integer topicId) {
        //获取用户订阅专题
        User loginUser = authenticationService.getLoginUser();
        Collection<Topic> myTopics;
        if (null != loginUser) {
            myTopics = topicService.getUserTopics(loginUser.getId());
        } else {
            myTopics = Collections.EMPTY_LIST;
        }
        
        return ModelAndViewBuilder.of("topicDetail")
                .setData("topic", topicService.findById(topicId))
                .setData("articles", topicService.getTopicArticles(topicId))
                .setData("myTopics", myTopics)
                .build();
    }
    
    
    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);
}
