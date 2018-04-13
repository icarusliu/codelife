package com.liuqi.tools.codelife.db.dao.mybatis.mapper;

import com.liuqi.tools.codelife.entity.Comment;
import com.liuqi.tools.codelife.entity.CommentType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 评论Mapper对象
 *
 * @Author: LiuQI
 * @Created: 2018/4/13 16:00
 * @Version: V1.0
 **/
@Mapper
public interface CommentMapper {
    /**
     * 通过评论类型及评论主体获取所有评论
     * @param type
     * @param destination
     * @return
     */
    Collection<Comment> getByDestination(@Param("type") CommentType type,
                                                @Param("destination") Integer destination);
    
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
