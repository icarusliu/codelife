package com.liuqi.tools.codelife.web.rest;

import com.liuqi.tools.codelife.db.entity.Comment;
import com.liuqi.tools.codelife.db.entity.CommentType;
import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 评论控制器
 *
 * @Author: LiuQI
 * @Created: 2018/4/13 16:08
 * @Version: V1.0
 **/
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @PostMapping("/add")
    public void add(@RequestParam("content") String content,
                    @RequestParam("anonymos") Boolean anonymos,
                    @RequestParam("type") String type,
                    @RequestParam("id") Integer id,
                    HttpServletRequest request) throws RestException {
        String host = request.getHeader("Host");
        User loginUser = authenticationService.getLoginUser();
        
        commentService.add(content, anonymos, ofType(type), id, loginUser, host);
    }
    
    /**
     * 将字符串转换成CommentType
     *
     * @param type
     * @return
     */
    private CommentType ofType(String type) {
        CommentType commentType;
        switch(type) {
            case "topic": commentType = CommentType.TOPIC; break;
            case "comment": commentType = CommentType.COMMENT; break;
            default: commentType = CommentType.ARTICLE;
        }
        
        return commentType;
    }
    
    @GetMapping("/findByDestination")
    public Collection<Comment> findByDestination(@RequestParam("type") String type,
                                                 @RequestParam("id") Integer id) {
        return commentService.findByDestination(ofType(type), id);
    }
}
