package com.liuqi.tools.codelife.service.impl;

import com.liuqi.tools.codelife.db.dao.CommentDao;
import com.liuqi.tools.codelife.entity.Comment;
import com.liuqi.tools.codelife.entity.CommentType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.CommentService;
import com.liuqi.tools.codelife.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 评论服务实现类
 *
 * @Author: LiuQI
 * @Created: 2018/4/13 17:18
 * @Version: V1.0
 **/
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;
    
    /**
     * 添加评论
     *
     * @param content
     * @param anonymos
     * @param commentType
     * @param id
     * @param loginUser
     * @param host
     */
    @Override
    public void add(String content, Boolean anonymos, CommentType commentType,
                    Integer id, User loginUser, String host) throws RestException {
        if (null == content || "".equals(content.trim())) {
            logger.error("Content cannot be null or empty!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "评论内容");
        }
        
        if (null == commentType) {
            logger.error("Comment type cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "评论类型");
        }
        
        if (null == id) {
            logger.error("Destination cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "评论对象");
        }
        
        if (null == host) {
            host = "";
        }
        
        Comment comment = new Comment();
        comment.setCommentTime(DateUtils.getNowDateStr());
        comment.setCommentUser(loginUser);
        comment.setAnonymos(anonymos);
        comment.setContent(content.trim());
        comment.setIp(host.trim());
        comment.setType(commentType);
        comment.setDestination(id);
    
        commentDao.add(comment);
    }
    
    @Override
    public Collection<Comment> findByDestination(CommentType commentType, Integer id) {
        return commentDao.getByDestination(commentType, id);
    }
    
    @Override
    public int getCommentCountByAuthor(Integer authorId) {
        return commentDao.getCommentCountByAuthor(authorId);
    }
    
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
}
