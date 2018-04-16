package com.liuqi.tools.codelife.db.dao;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.exceptions.RestException;

import java.util.List;

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
     * @param typeId
     * @return 返回查找的所有文章，如果无则返回空的List对象
     */
    List<Article> findByAuthor(int authorId, Integer typeId);
    
    /**
     * 查找所有的文章
     * @return 返回查找的所有文章，如果无则返回空的List对象
     */
    List<Article> findAll();

    /**
     * 保存文章对象
     *
     * @param article 需要保存的文章对象
     */
    Integer save(Article article);
    
    /**
     * 通过分类查找该分类下的所有文章
     *
     * @param typeId
     * @return 当没有数据时返回空的ArrayList对象
     */
    List<Article> findByForum(Integer typeId);
    
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
     *  @param id
     * @param title
     * @param type
     * @param forumId
     */
    void updateArticle(Integer id, String title, Integer type, Integer forumId);
    
    
    /**
     * 按ReadCount及CreateDate排序返回前指定个数和文章
     *
     * @param i 需要返回的文章数
     * @return 查找的文章清单，如果无文章将返回一个空的List
     */
    List<Article> findTopArticles(int i);
    
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
    List<Article> findByTopic(Integer id);
    
    /**
     * 通过标题关键字搜索文章
     *
     * @param key
     * @return
     */
    List<Article> searchByTitleKey(String key);
    
    /**
     * 文章置顶及取消置顶
     *
     * @param id
     * @param fixTop
     */
    void updateFixTop(Integer id, boolean fixTop);
    
    /**
     * 设置文章是否推荐
     *
     * @param id
     * @param b
     */
    void updateRecommended(Integer id, boolean b);
}
