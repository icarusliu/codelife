package com.liuqi.tools.codelife.controllers.rest;

import com.liuqi.tools.codelife.entity.*;
import com.liuqi.tools.codelife.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.tool.FileUtils;
import com.liuqi.tools.codelife.tool.MapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RestController
@RequestMapping("/system/topicManager")
public class TopicManagerController {
    @Autowired
    private TopicService topicService;
    
    @Value("${app.file.topic.savePath}")
    private String saveFilePath;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    /**
     * 获取专题清单
     * 管理员能够看所有的清单，而专题管理员只能看它是管理员的清单
     *
     * @return
     */
    @RequestMapping("/getAll")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TOPIC_ADMIN')")
    public List<Topic> getAll(@RequestParam(value = "nowPage", required = false) Integer nowPage,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        User loginUser = authenticationService.getLoginUser();
        if (loginUser.isSystemAdmin()) {
            return topicService.findAll(null == nowPage ? 1 : nowPage, null == pageSize ? 20 : pageSize).getList();
        }
        
        return topicService.findByAdmin(loginUser);
    }
    
    /**
     * 获取专题订阅用户列表
     *
     * @param topicId
     * @return
     */
    @RequestMapping("/getTopicUsers")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TOPIC_ADMIN')")
    public Collection<User> getTopicUsers(@RequestParam("topicId") Integer topicId) {
        return topicService.getTopicUsers(topicId);
    }
    
    /**
     * 新增专题
     * 只有系统管理员才能新增专题
     *
     * @param title
     * @param introduction
     * @param imgFile 封面图片文件
     */
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
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
     * 系统管理员和专题管理员都可以更新专题
     *
     * @param title
     * @param introduction
     * @param type
     * @param admin
     * @param imgFile
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TOPIC_ADMIN')")
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
     * 修改专题状态到正常状态
     * 只有系统管理员能够修改专题状态
     */
    @PostMapping("/setStatusNormal")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void setStatusNormal(@RequestParam("id")Integer id) {
        topicService.updateStatus(id, TopicStatus.NORMAL);
    }
    
    /**
     * 批量将文章加入专题
     * 系统管理员与专题管理员均可处理
     *
     * @param id
     * @param articles
     * @throws RestException
     */
    @RequestMapping("/addArticles")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TOPIC_ADMIN')")
    public void addArticles(@RequestParam("id") Integer id, @RequestParam("articles")String articles) throws RestException {
        topicService.addTopicArticls(id, Arrays.stream(articles.split(","))
                .map(Integer::valueOf).collect(Collectors.toList()));
    }

    /**
     * 获取编辑专题时需要的数据
     *
     * @param topicId
     * @return
     */
    @PostMapping("/getEditInfo")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TOPIC_ADMIN')")
    public Map<String, Object> getEditInfo(@RequestParam("topicId") Integer topicId) {
        return MapBuilder.of()
                .put("topic", topicService.findById(topicId))
                .put("articles", topicService.getTopicArticles(topicId))
                .put("types", TopicType.values())
                .build();
    }
    
    /**
     * 删除专题
     * 只有系统管理员可以删除专题
     *
     * @param id
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(@RequestParam("id")Integer id) throws RestException {
        topicService.deleteTopic(id);
    }
    
    /**
     * 添加用户到专题
     *
     * @param topicId
     * @param userId
     */
    @PostMapping("/addUserToTopic")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TOPIC_ADMIN')")
    public void addUserToTopic(@RequestParam("topicId") Integer topicId,
                               @RequestParam("userId") Integer userId) throws RestException {
        checkAdmin(topicId);
        
        topicService.subscribeTopic(userId, topicId);
    }
    
    /**
     * 检查当前登录用户是否是专题管理员
     *
     * @param topicId
     * @throws RestException
     */
    private void checkAdmin(Integer topicId) throws RestException {
        //先判断登录用户是否是该Topic的管理员
        Topic topic = topicService.findById(topicId);
        if (null == topic) {
            logger.error("Topic does not exist, topic Id: " + topicId);
            throw ExceptionTool.getException(ErrorCodes.TOPIC_NOT_EXISTS, topicId);
        }
        
        User loginUser = authenticationService.getLoginUser();
        //如果不是系统管理员，并且Topic的管理员为空或者不为空且不与当前登录用户相同，那么不能修改
        boolean canNotEdit = !loginUser.isSystemAdmin() && (topic.getAdmin() == null || loginUser.getId() != topic.getAdmin().getId());
        if (canNotEdit) {
            logger.error("Topic admin is not current user, current user: " + loginUser + ", admin: " + topic.getAdmin());
            throw ExceptionTool.getException(ErrorCodes.TOPIC_MANAGER_EDIT_NOT_MANAGER);
        }
    }
    
    
    /**
     * 从专题中删除用户
     *
     * @param topicId
     * @param userId
     */
    @PostMapping("/removeUserFromTopic")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TOPIC_ADMIN')")
    public void removeUserFromTopic(@RequestParam("topicId") Integer topicId,
                               @RequestParam("userId") Integer userId) throws RestException {
        checkAdmin(topicId);
        
        topicService.unSubscribeTopic(userId, topicId);
    }

    
    /**
     * 删除专题下的文章
     * 系统管理员与专题管理员都可以处理
     *
     * @param id
     * @param articleId
     */
    @RequestMapping("/deleteArticle")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TOPIC_ADMIN')")
    public void deleteArticle(@RequestParam("id") Integer id, @RequestParam("articleId") Integer articleId) {
        topicService.deleteTopicArticle(id, articleId);
    }
    
    private static final Logger logger = LoggerFactory.getLogger(TopicManagerController.class);
    
}
