package com.liuqi.tools.codelife.db.dao;

import com.liuqi.tools.codelife.db.entity.ArticleType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 文章分类Mapper
 *
 * @Author: LiuQI
 * @Created: 2018/3/23 21:01
 * @Version: V1.0
 **/
@Mapper
public interface ArticleTypeDao {
    /**
     * 根据Type的ID获取Type
     *
     * @param id
     * @return 返回获取的Type
     */
    ArticleType findById(@Param("id") int id);
    
    /**
     * 通过分类名称查找分类
     *
     * @param userId
     * @param typeName
     * @return
     */
    ArticleType findByName(@Param("userId") Integer userId, @Param("name") String typeName);
    
    /**
     * 查找用户的分类
     * @param userId
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

    /**
     * 更新分类文章数量
     */
    void updateArticleCounts(@Param("dataList") List<Map<String, Object>> dataList);

    /**
     * 根据文章分类统计文章数目
     * @return  文章分类及对应的文章数目
     */
    List<Map<String,Object>> countArticleByType();
}
