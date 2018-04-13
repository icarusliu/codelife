package com.liuqi.tools.codelife.db.dao;

import com.liuqi.tools.codelife.entity.Comment;
import com.liuqi.tools.codelife.entity.CommentType;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 评论数据库操作接口
 */
public interface CommentDao {
    /**
     * 通过评论类型及评论主体获取所有评论
     * @param type
     * @param destination
     * @return
     */
    Collection<Comment> getByDestination(CommentType type,
                                         Integer destination);
    
    /**
     * 添加评论
     * @param comment
     */
    void add(Comment comment);
    
    /**
     * 删除评论
     * @param comment
     */
    void delete(Comment comment);
    
    /**
     * 更新评论内容
     *
     * @param id
     * @param content
     */
    void update(@Param("id") Integer id, @Param("content") String content);
}
