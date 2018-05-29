package com.liuqi.tools.codelife.db.dao.mybatis;

import com.liuqi.tools.codelife.db.dao.ArticleTypeDao;
import com.liuqi.tools.codelife.db.dao.mybatis.mapper.ArticleTypeMapper;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * MyBatis文章分类操作类
 *
 * @Author: LiuQI
 * @Created: 2018/4/16 13:03
 * @Version: V1.0
 **/
@Repository
public class MyBatisArticleTypeDao implements ArticleTypeDao {
    @Autowired
    private ArticleTypeMapper typeMapper;
    
    @Override
    public ArticleType findById(int id) {
        return typeMapper.findById(id);
    }
    
    @Override
    public void add(ArticleType type) {
        typeMapper.add(type);
    }
    
    @Override
    public void rename(Integer id, String name) {
        typeMapper.rename(id, name);
    }
    
    @Override
    public void addArticleCount(Integer id) {
        typeMapper.addArticleCount(id);
    }
    
    @Override
    public void deduceArticleCount(Integer id) {
        typeMapper.deduceArticleCount(id);
    }
    
    @Override
    public List<ArticleType> findByUser(User user) {
        return Optional.ofNullable(typeMapper.findByUser(user.getId())).orElse(Collections.EMPTY_LIST);
    }
    
    @Override
    public List<ArticleType> findAllSystemTypes() {
        return Optional.ofNullable(typeMapper.findByUser(1)).orElse(Collections.EMPTY_LIST);
    }
    
    @Override
    public ArticleType findByName(String typeName) {
        return typeMapper.findByName(typeName);
    }
}
