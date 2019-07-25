package com.liuqi.tools.codelife.db.dao;

import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;

import java.util.List;

/**
 * 文章分类数据库操作类
 */
public interface ArticleTypeDao {
    /**
     * 根据ID查找文章分类
     *
     * @return 返回查找的文章分类 如果未找着会返回空值
     */
    ArticleType findById(int id);
    
    /**
     * 通过分类名称查找分类
     *
     * @param typeName 分类名称
     * @return 返回查找到的分类对象，如果没有返回为空
     */
    ArticleType findByName(String typeName);
    
    /**
     * 通过用户查找其分类
     * @param user
     * @return
     */
    List<ArticleType> findByUser(User user);
    
    /**
     * 查找所有系统版块
     *
     * @return
     */
    List<ArticleType> findAllSystemTypes();
    
    /**
     * 新增文章分类
     *
     * @param type
     */
    void add(ArticleType type);
    
    /**
     * 更新分类名称
     *
     */
    void rename(Integer id, String name);
    
    /**
     * 分类下文章数加1
     * @param id
     */
    void addArticleCount(Integer id);
    
    /**
     * 分类下文章数减1
     * @param id
     */
    void deduceArticleCount(Integer id);
}
