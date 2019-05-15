package com.liuqi.tools.codelife.web.rest;

import com.liuqi.tools.codelife.db.entity.ArticleComment;
import com.liuqi.tools.codelife.db.entity.CommentType;
import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import com.liuqi.tools.codelife.service.AuthenticationService;
import com.liuqi.tools.codelife.service.ArticleCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * 评论控制器
 *
 * @Author: LiuQI
 * @Created: 2018/4/13 16:08
 * @Version: V1.0
 **/
@RestController
@RequestMapping("/article/comment")
public class ArticleCommentController {
    @Autowired
    private ArticleCommentService commentService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @PostMapping("/add")
    public void add(@RequestParam("content") String content,
                    @RequestParam("anonymos") Boolean anonymos,
                    @RequestParam("articleId") Integer articleId,
                    @RequestParam(value = "parent", required = false) Integer parent,
                    @RequestParam("showName") String showName,
                    HttpServletRequest request) throws RestException {
        String host = request.getHeader("Host");
        User loginUser = authenticationService.getLoginUser();
        
        commentService.add(content, anonymos, articleId, parent, loginUser, host, showName);
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
    
    @GetMapping("/findByArticle")
    public Collection<ArticleComment> findByArticle(@RequestParam("articleId") Integer articleId) {
        return commentService.findByArticle(articleId);
    }

    @GetMapping("/latestComments")
    public List<ArticleComment> latestComments() {
        return commentService.findLatestComments(5);
    }
}
