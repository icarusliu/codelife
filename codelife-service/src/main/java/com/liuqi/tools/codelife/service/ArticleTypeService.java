package com.liuqi.tools.codelife.service;

import com.liuqi.tools.codelife.db.entity.ArticleType;
import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.util.exceptions.RestException;

import java.util.List;

/**
 * 文章分类服务类
 * @author qi.liu
 */
public interface ArticleTypeService {
    /**
     * 通过用户获取其分类清单
     *
     * @param user
     * @return
     */
    List<ArticleType> findByUser(User user);
    
    /**
     * 获取系统版块清单，系统板块清单与文章分类存储在一块，但它的所属用户为Admin
     *
     * @return
     */
    List<ArticleType> findSystemTypes();
    
    /**
     * 重命名分类
     *
     * @param id 分类ID
     * @param name 新的分类名称
     * @throws RestException
     */
    void saveType(Integer id, String name) throws RestException;
    
    /**
     * 保存文件类型
     *
     * @param name
     * @throws RestException
     */
    void saveType(String name) throws RestException;
    
    /**
     * 通过ID获取Type
     * @param id Type的ID
     * @return 返回获取的Type对象
     * @throws RestException
     */
    ArticleType findById(int id) throws RestException;
    
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
