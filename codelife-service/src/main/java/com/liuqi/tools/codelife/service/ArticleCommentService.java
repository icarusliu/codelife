package com.liuqi.tools.codelife.service;

import com.liuqi.tools.codelife.db.entity.ArticleComment;
import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.util.exceptions.RestException;

import java.util.Collection;
import java.util.List;

/**
 * 评论服务接口
 * @author qi.liu
 */
public interface ArticleCommentService {
    /**
     * 添加评论
     * @param content
     * @param anonymos
     * @param loginUser
     * @param host
     * @param showName
     * @throws RestException
     */
    void add(String content, Boolean anonymos,
             Integer parentCommentId, Integer articleId,
             User loginUser, String host, String showName) throws RestException;
    
    /**
     * 根据文章及父级评论查询评论
     *
     * @param articleId
     * @return
     */
    Collection<ArticleComment> findByArticle(Integer articleId);
    
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
    List<ArticleComment> findLatestComments(int count);
}
