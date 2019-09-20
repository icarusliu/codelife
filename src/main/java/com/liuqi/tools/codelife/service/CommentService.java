package com.liuqi.tools.codelife.service;

import com.liuqi.tools.codelife.entity.Comment;
import com.liuqi.tools.codelife.entity.CommentType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 评论服务接口
 */
public interface CommentService {
    /**
     * 添加评论
     * @param content
     * @param anonymos
     * @param commentType
     * @param id
     * @param loginUser
     * @param host
     */
    void add(String content, Boolean anonymos, CommentType commentType, Integer id, User loginUser, String host) throws RestException;
    
    /**
     * 根据评论类型以及目标IP获取评论清单
     *
     * @param commentType
     * @param id
     * @return
     */
    Collection<Comment> findByDestination(CommentType commentType, Integer id);
    
    /**
     * 根据作者获取它的文章的评论总数
     *
     * @param authorId
     * @return
     */
    int getCommentCountByAuthor(Integer authorId);
}
