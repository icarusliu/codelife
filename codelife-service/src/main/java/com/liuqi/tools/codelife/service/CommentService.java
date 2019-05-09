package com.liuqi.tools.codelife.service;

import com.liuqi.tools.codelife.db.entity.Comment;
import com.liuqi.tools.codelife.db.entity.CommentType;
import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.util.exceptions.RestException;

import java.util.Collection;
import java.util.List;

/**
 * 评论服务接口
 * @author qi.liu
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
     * @throws RestException
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

    /**
     * 获取新增加的评论列表
     * @param count 需要查询的评论数目
     * @return  新增加的评论列表
     */
    List<Comment> findNewerComments(int count);
}
