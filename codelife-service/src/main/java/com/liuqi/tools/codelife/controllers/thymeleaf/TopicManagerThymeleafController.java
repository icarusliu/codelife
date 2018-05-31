package com.liuqi.tools.codelife.controllers.thymeleaf;

import com.liuqi.tools.codelife.entity.*;
import com.liuqi.tools.codelife.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.tool.FileUtils;
import com.liuqi.tools.codelife.tool.MapBuilder;
import com.liuqi.tools.codelife.tool.ModelAndViewBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 专题管理控制器
 *
 * @Author: LiuQI
 * @Created: 2018/4/3 10:27
 * @Version: V1.0
 **/
@Controller
@RequestMapping("/system/topicManager")
public class TopicManagerThymeleafController {
    @Autowired
    private TopicService topicService;
    
    @Value("${app.file.topic.savePath}")
    private String saveFilePath;
    
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * 专题管理页面
     * 只有系统管理员或者是专题管理员才能进入专题管理页面
     *
     * @return
     */
    @RequestMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TOPIC_ADMIN')")
    public ModelAndView topicManager() {
        return ModelAndViewBuilder.of("system/topicManager")
                .setData("topicTypes", TopicType.values())
                .build();
    }
    
    /**
     * 修改专题
     * 系统管理员及专题管理员均可处理
     *
     * @param id
     */
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TOPIC_ADMIN')")
    public ModelAndView edit(@RequestParam("id") Integer id) {
        Topic topic = topicService.findById(id);
        Collection<Article> articles = topicService.getTopicArticles(id);
        
        return ModelAndViewBuilder.of("system/topicEdit")
                .setData("topic", topic)
                .setData("articles", articles)
                .setData("types", TopicType.values())
                .build();
    }
}
