package com.liuqi.tools.codelife.db.dao;

import com.liuqi.tools.codelife.db.entity.ArticleComment;
import com.liuqi.tools.codelife.db.entity.CommentType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 评论Mapper对象
 *
 * @Author: LiuQI
 * @Created: 2018/4/13 16:00
 * @Version: V1.0
 **/
@Mapper
public interface ArticleCommentDao {
    /**
     * 通过评论类型及评论主体获取所有评论
     * @param articleId
     * @return
     */
    Collection<ArticleComment> findByArticle(@Param("article") Integer articleId);

    ArticleComment findById(@Param("id") Integer id);
    
    /**
     * 添加评论
     * @param articleComment
     */
    void add(ArticleComment articleComment);
    
    /**
     * 删除评论
     * @param articleComment
     */
    void delete(ArticleComment articleComment);
    
    /**
     * 更新评论内容
     *
     * @param id
     * @param content
     */
    void update(@Param("id") Integer id, @Param("content") String content);
    
    /**
     * 根据作者获取它的文章的评论总数
     *
     * @param authorId
     * @return
     */
    int getCommentCountByAuthor(@Param("authorId") Integer authorId);

    /**
     * 获取新增加的评论列表
     *
     * @param count 需要查询的评论数目
     * @return 新增加的评论列表
     */
    List<ArticleComment> findNewerComments(@Param("count") int count);
}
