package com.liuqi.tools.codelife.controllers;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.Topic;
import com.liuqi.tools.codelife.entity.TopicType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.util.FileUtils;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 专题管理控制器
 * 只有系统管理员才能管理专题
 *
 * @Author: LiuQI
 * @Created: 2018/4/3 10:27
 * @Version: V1.0
 **/
@Controller
@RequestMapping("/system/topicManager")
@PreAuthorize("hasAuthority('ADMIN')")
public class TopicManagerController {
    @Autowired
    private TopicService topicService;
    
    @Value("${app.file.topic.savePath}")
    private String saveFilePath;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    /**
     * 专题管理页面
     * @return
     */
    @RequestMapping
    public ModelAndView topicManager() {
        return ModelAndViewBuilder.of("system/topicManager")
                .setData("topicTypes", TopicType.values())
                .build();
    }
    
    /**
     * 新增专题
     * @param title
     * @param introduction
     * @param imgFile 封面图片文件
     */
    @RequestMapping("/add")
    @ResponseBody
    public void add(@RequestParam("title") String title, @RequestParam("introduction") String introduction ,
                    @RequestParam("type") Integer type,
                    @RequestParam(value = "img", required = false)MultipartFile imgFile) throws RestException {
        String fileName = "";
        
        //先将文件保存到某个目录
        if (null != imgFile) {
            fileName = FileUtils.saveToFile(imgFile, saveFilePath);
        }
        
        TopicType topicType = TopicType.valueOf(type);
        
        //获取创建用户
        User loginUser = authenticationService.getLoginUser();
        
        //调用保存服务将Topic存入数据库
        try {
            topicService.addTopic(title, introduction, fileName, topicType, loginUser);
        } catch (Exception ex) {
            //如果出现异常需要将保存的文件删除
            FileUtils.deleteFile(saveFilePath, fileName);
            
            throw ex;
        }
    }
    
    /**
     * 更新专题
     *
     * @param title
     * @param introduction
     * @param type
     * @param admin
     * @param imgFile
     */
    @RequestMapping("/update")
    @ResponseBody
    public void update(
                @RequestParam("id") Integer id,
                @RequestParam("title") String title, @RequestParam("introduction") String introduction ,
                       @RequestParam("type") Integer type,
                       @RequestParam(value = "admin", required = false) Integer admin,
                       @RequestParam(value = "img", required = false)MultipartFile imgFile) throws RestException {
        String fileName = "";
    
        //先将文件保存到某个目录
        if (null != imgFile) {
            fileName = FileUtils.saveToFile(imgFile, saveFilePath);
        }
        
        TopicType topicType = TopicType.valueOf(type);
        topicService.updateTopic(id, title, introduction, fileName, topicType, admin);
    }
    
    /**
     * 批量将文章加入专题
     *
     * @param id
     * @param articles
     * @throws RestException
     */
    @RequestMapping("/addArticles")
    @ResponseBody
    public void addArticles(@RequestParam("id") Integer id, @RequestParam("articles")String articles) throws RestException {
        topicService.addTopicArticls(id, Arrays.stream(articles.split(","))
                .map(item -> Integer.valueOf(item)).collect(Collectors.toList()));
    }
    
    /**
     * 修改专题
     * @param id
     */
    @RequestMapping("/edit")
    public ModelAndView edit(@RequestParam("id") Integer id) {
        Topic topic = topicService.findById(id);
        Collection<Article> articles = topicService.getTopicArticles(id);
        
        return ModelAndViewBuilder.of("system/topicEdit")
                .setData("topic", topic)
                .setData("articles", articles)
                .setData("types", TopicType.values())
                .build();
    }
    
    /**
     * 删除专题
     *
     * @param id
     */
    @RequestMapping("/delete")
    @ResponseBody
    public void delete(@RequestParam("id")Integer id) throws RestException {
        topicService.deleteTopic(id);
    }
    
    /**
     * 删除专题下的文章
     *
     * @param id
     * @param articleId
     */
    @RequestMapping("/deleteArticle")
    @ResponseBody
    public void deleteArticle(@RequestParam("id") Integer id, @RequestParam("articleId") Integer articleId) {
        topicService.deleteTopicArticle(id, articleId);
    }
}
