package com.liuqi.tools.codelife.db.dao.mybatis.mapper;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章分类Mapper
 *
 * @Author: LiuQI
 * @Created: 2018/3/23 21:01
 * @Version: V1.0
 **/
@Mapper
public interface ArticleTypeMapper {
    /**
     * 根据Type的ID获取Type
     * @return 返回获取的Type
     */
    ArticleType findById(@Param("id") int id);
    
    /**
     * 通过分类名称查找分类
     * @param typeName
     */
    ArticleType findByName(@Param("name") String typeName);
    
    /**
     * 查找用户的分类
     *
     * @return
     */
    List<ArticleType> findByUser(@Param("userId") Integer userId);
    
    /**
     * 保存文章分类
     *
     * @param type
     */
    void add(ArticleType type);
    
    /**
     * 重命名分类
     * @param id 分类ID
     * @param name 分类名称
     */
    void rename(@Param("id") Integer id, @Param("name") String name);
    
    /**
     * 增加文章数目
     * @param id
     */
    void addArticleCount(@Param("id") Integer id);
    
    /**
     * 文章数目减1
     *
     * @param id
     */
    void deduceArticleCount(@Param("id") Integer id);
}
