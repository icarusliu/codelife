package com.liuqi.tools.codelife.web.rest;

import com.liuqi.tools.codelife.db.entity.Article;
import com.liuqi.tools.codelife.db.entity.Topic;
import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.util.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.util.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.TopicService;
import com.liuqi.tools.codelife.util.FileUtils;
import com.liuqi.tools.codelife.util.MapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

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
@RestController
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Value("${app.file.topic.savePath}")
    private String saveFilePath;

    /**
     * 获取REST模式下专题浏览页面需要的数据
     */
    @PostMapping("/getExplorerData")
    public Map<String, Object> getExplorerData(@RequestParam(value = "key", required = false) String key) {
        //获取用户订阅专题及其更新的文章
        User loginUser = authenticationService.getLoginUser();
        Collection<Topic> myTopics;
        if (null != loginUser) {
            myTopics = topicService.getUserTopics(loginUser.getId());
        } else {
            myTopics = new ArrayList<>(0);
        }

        // 获取热门专题
        List<Topic> hotTopics = topicService.findHotTopics(5);
    
        //获取所有的未订阅并且类型是开放的专题，私有的专题不能直接展现
        List<Topic> topics = topicService.findUserNotSubscribed(loginUser, key, 1, 20).getList();
        return MapBuilder.of()
                .put("myTopics", myTopics)
                .put("topics", topics)
                .put("hotTopics", hotTopics)
                .build();
    }
    
    /**
     * 获取首页需要展示的专题数据
     *
     * @return
     */
    @PostMapping("/getForIndex")
    public Map<String, Object> getForIndex() {
        //获取用户订阅专题及其更新的文章
        Collection<Topic> myTopics = getMyTopics();
        User loginUser = authenticationService.getLoginUser();
    
        //获取所有的未订阅并且类型是开放的专题，私有的专题不能直接展现
        List<Topic> topics = topicService.findUserNotSubscribed(loginUser, "", 1, 20).getList();
    
        return MapBuilder.of().put("myTopic", myTopics)
                .put("topics", topics)
                .build();
    }

    /**
     * 获取登录用户订阅的专题清单
     * @return  登录用户订阅的专题清单，如果未登录返回为空的List
     */
    private Collection<Topic> getMyTopics() {
        User loginUser = authenticationService.getLoginUser();
        Collection<Topic> myTopics;
        if (null != loginUser) {
            myTopics = topicService.getUserTopics(loginUser.getId());
        } else {
            myTopics = new ArrayList<>(0);
        }
        if (0 != myTopics.size()) {
            myTopics.forEach(topic -> topic.setArticles(topicService.getTopicArticles(topic.getId())));
        }

        return myTopics;
    }

    @GetMapping("/getForDetail")
    public Map<String, Object> getForDetail(@RequestParam("id") Integer id) {
        return MapBuilder.of()
                .put("topic", topicService.findById(id))
                .put("hotTopics", topicService.findHotTopics(6))
                .put("myTopics", getMyTopics())
                .build();
    }

    /**
     * 获取专题下的文章
     *
     * @param topicId
     * @return
     */
    @RequestMapping("/getTopicArticles")
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
    @PreAuthorize("isAuthenticated()")
    public void unSubscribe(@RequestParam("id") Integer topicId) throws RestException {
        User loginUser = authenticationService.getLoginUser();
        
        topicService.unSubscribeTopic(loginUser.getId(), topicId);
    }
    
    @RequestMapping("/getImg")
    public void getImg(@RequestParam("fileName") String fileName, OutputStream outputStream) throws RestException {
        FileUtils.outputFileContent(saveFilePath, fileName, outputStream);
    }
    
    /**
     * 返回REST方式下专题详细页面的数据
     *
     * @param topicId
     * @return
     */
    @PostMapping("/getMyTopics")
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
            myTopics = new ArrayList(0);
        }

        List<Topic> hotTopics = topicService.findHotTopics(5);
    
        //如果专题编号未传入，且用户已经登录，则获取用户订阅的第一个专题进行展示
        Topic topic = null;
        if (null == topicId && null != loginUser) {
            List<Topic> topics = topicService.getUserTopics(loginUser.getId());
            if (null != topics && 0 < topics.size()) {
                topic = topics.get(0);
                topicId = topic.getId();
            }
        } else if (null != topicId){
            topic = topicService.findById(topicId);
        } else if (!CollectionUtils.isEmpty(hotTopics)){
            topic = hotTopics.get(0);
            topicId = topic.getId();
        }
    
        //获取专题订阅用户清单
        List<User> userList;
        if (null != topicId) {
            userList = topicService.getTopicUsers(topicId);
        } else {
            userList = new ArrayList(0);
        }
    
        //获取专题文章清单
        List<Article> articleList;
        if (null != topicId) {
            articleList = topicService.getTopicArticles(topicId);
        } else {
            articleList = new ArrayList(0);
        }
    
        return MapBuilder.of()
                .put("topic", topic)
                .put("articles", articleList)
                .put("myTopics", myTopics)
                .put("userList", userList)
                .put("hotTopics", hotTopics)
                .build();
    }

    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);
}
