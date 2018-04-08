package com.liuqi.tools.codelife.db.dao;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;

import java.util.Collection;

/**
 * 文章操作Dao
 *
 * @Author: LiuQI
 * @Created: 2018/3/26 17:15
 * @Version: V1.0
 **/
public interface ArticleDao {
    /**
     * 查找文章
     * @param id
     * @throws RestException 当指定编号的文章不存在时抛出异常
     * @return 返回查找的文章
     */
    Article findById(int id) throws RestException;
    
    /**
     * 查找某个用户所写的所有文章
     *
     * @param authorId 用户编号
     * @return 返回查找的所有文章，如果无则返回空的List对象
     */
    Collection<Article> findByAuthor(int authorId);
    
    /**
     * 查找所有的文章
     * @return 返回查找的所有文章，如果无则返回空的List对象
     */
    Collection<Article> findAll();
    
    /**
     * 查找所有文章分类
     *
     * @return 返回所有文章分类，如果无则返回空的List对象
     */
    Collection<ArticleType> findAllTypes();
    
    /**
     * 根据ID查找文章分类
     *
     * @return 返回查找的文章分类 如果未找着会返回空值
     */
    ArticleType findTypeById(int id);
    
    /**
     * 保存文章对象
     *
     * @param article 需要保存的文章对象
     */
    void save(Article article);
    
    /**
     * 通过分类查找该分类下的所有文章
     *
     * @param typeId
     * @return 当没有数据时返回空的ArrayList对象
     */
    Collection<Article> findByType(Integer typeId);
    
    /**
     * 保存文章分类
     *
     * @param type
     */
    void saveType(ArticleType type);
    
    /**
     * 通过分类名称查找分类
     *
     * @param typeName 分类名称
     * @return 返回查找到的分类对象，如果没有返回为空
     */
    ArticleType findTypeByName(String typeName);
    
    /**
     * 对应ID的文章的阅读次数加1
     *
      * @param id 文章ID
     */
    void addReadCount(int id);
    
    /**
     * 删除对应的文章
     *
     * @param id
     */
    void deleteArticle(Integer id);
    
    /**
     * 更新文章
     *
     * @param id
     * @param title
     * @param type
     */
    void updateArticle(Integer id, String title, Integer type);
    
    /**
     * 重命名分类
     *
     * @param id
     * @param name
     */
    void renameType(Integer id, String name);
    
    /**
     * 按ReadCount及CreateDate排序返回前指定个数和文章
     *
     * @param i 需要返回的文章数
     * @return 查找的文章清单，如果无文章将返回一个空的List
     */
    Collection<Article> findTopArticles(int i);
    
    /**
     * 为文章的点赞数增加一个值
     * 这个值可以是1或者-1，1时为点赞、-1时为取消点赞
     *
     * @param id 文章ID
     * @param i 变动的点赞数，可以是1或者是-1
     */
    void addPraiseCount(int id, int i);
    
    /**
     * 获取专题下的所有文章
     *
      * @param id
     * @return
     */
    Collection<Article> findByTopic(Integer id);
    
    /**
     * 通过标题关键字搜索文章
     *
     * @param key
     * @return
     */
    Collection<Article> searchByTitleKey(String key);
}
