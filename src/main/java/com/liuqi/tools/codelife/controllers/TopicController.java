package com.liuqi.tools.codelife.controllers;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.util.FileUtils;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.OutputStream;
import java.util.Collection;

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
        //获取所有专题列表
        Collection<Topic> topics = topicService.findAll();
        
        return ModelAndViewBuilder.of("topic")
                .setData("myTopics", topics)
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
    
    @RequestMapping("/getImg")
    @ResponseBody
    public void getImg(@RequestParam("fileName") String fileName, OutputStream outputStream) throws RestException {
        FileUtils.outputFileContent(saveFilePath, fileName, outputStream);
    }
}
