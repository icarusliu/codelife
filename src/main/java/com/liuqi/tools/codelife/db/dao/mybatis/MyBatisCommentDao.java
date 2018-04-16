package com.liuqi.tools.codelife.db.dao.mybatis;

import com.liuqi.tools.codelife.db.dao.CommentDao;
import com.liuqi.tools.codelife.db.dao.mybatis.mapper.CommentMapper;
import com.liuqi.tools.codelife.entity.Comment;
import com.liuqi.tools.codelife.entity.CommentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * MyBatis评论操作类
 * @Author: LiuQI
 * @Created: 2018/4/13 16:06
 * @Version: V1.0
 **/
@Repository
public class MyBatisCommentDao implements CommentDao {
    @Autowired
    private CommentMapper commentMapper;
    
    @Override
    public Collection<Comment> getByDestination(CommentType type, Integer destination) {
        return Optional.ofNullable(commentMapper.getByDestination(type, destination)).orElse(Collections.EMPTY_LIST);
    }
    
    @Override
    public void add(Comment comment) {
        commentMapper.add(comment);
    }
    
    @Override
    public void delete(Comment comment) {
        commentMapper.delete(comment);
    }
    
    @Override
    public void update(Integer id, String content) {
        commentMapper.update(id, content);
    }
    
    @Override
    public int getCommentCountByAuthor(Integer authorId) {
        return commentMapper.getCommentCountByAuthor(authorId);
    }
}
