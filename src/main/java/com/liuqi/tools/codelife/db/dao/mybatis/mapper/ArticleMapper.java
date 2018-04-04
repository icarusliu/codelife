package com.liuqi.tools.codelife.db.dao.mybatis.mapper;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 文章Mapper
 *
 * @Author: LiuQI
 * @Created: 2018/3/23 21:01
 * @Version: V1.0
 **/
@Mapper
public interface ArticleMapper {
    /**
     * 查找所有文章清单
     *
     * @return
     */
    Collection<Article> findAll();
    
    /**
     * 通过分类查找分类下的所有文章
     *
     * @param typeId
     * @return
     */
    Collection<Article> findByType(int typeId);
    
    /**
     * 通过ID获取文章对象
     *
     * @param id 文章ID
     * @return 如果ID不存在，返回空对象
     */
    Article findById(int id);
    
    /**
     * 通过用户查找文章清单
     *
     * @param authorID 用户ID
     * @return 返回该用户下的文章清单
     */
    Collection<Article> findByAuthor(int authorID);
    
    /**
     * 返回所有文章分类
     *
     * @return
     */
    Collection<ArticleType> findAllTypes();
    
    /**
     * 根据Type的ID获取Type
     * @return 返回获取的Type
     */
    ArticleType findTypeById(int id);
    
    /**
     * 保存文章
     *
     * @param article 需要保存的文章
     */
    void save(Article article);
    
    /**
     * 保存文章分类
     *
     * @param type
     */
    void saveType(ArticleType type);
    
    /**
     * 通过分类名称查找分类
     * @param typeName
     */
    ArticleType findTypeByName(String typeName);
    
    /**
     * 对应文章的阅读次数加1
     *
     * @param id 文章ID
     */
    void addReadCount(int id);
    
    /**
     * 根据文章ID删除文章
     *
     * @param id
     */
    void deleteArticle(Integer id);
    
    /**
     * 更新文章标题、分类等信息
     *
     * @param id
     * @param title
     * @param type
     */
    void updateArticle(@Param("id") Integer id, @Param("title") String title, @Param("type") Integer type);
    
    /**
     * 重命名分类
     * @param id 分类ID
     * @param name 分类名称
     */
    void renameType(@Param("id") Integer id, @Param("name") String name);
    
    /**
     * 按ReadCount及CreateDate排序返回前指定个数的文章
     *
     * @param i 需要返回的文章数
     * @return 获取的文章清单，如果无则返回空的List
     */
    Collection<Article> findTopArticles(int i);
    
    /**
     * 点赞
     * 当前用户是否对某篇文章点赞，存储在Cookie中，数据库中只存储点赞数
     *
     * @param id 点赞的编号
     * @param count 点赞次数，可以是1或者-1，-1时表示是取消点赞
     */
    void addPraiseCount(@Param("id") int id, @Param("count") int count);
    
    
    /**
     * 获取专题下的文章
     * @param id 专题编号
     * @return
     */
    Collection<Article> findByTopic(@Param("id") Integer id);
}
