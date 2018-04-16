package com.liuqi.tools.codelife.db.dao.mybatis.mapper;

import com.liuqi.tools.codelife.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    List<Article> findAll();
    
    /**
     * 通过分类查找分类下的所有文章
     *
     * @param forumId
     * @return
     */
    List<Article> findByForum(@Param("forumId")int forumId);
    
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
     * @param typeId 可为空
     * @return 返回该用户下的文章清单
     */
    List<Article> findByAuthor(@Param("authorId") int authorID, @Param("typeId") Integer typeId);
    
    /**
     * 根据用户统计相关数据
     * 包含：用户文章的阅读总数、用户文章的点赞数
     * @param authorId
     * @return
     */
    UserArticleStatInfo getStatisticInfoByAuthor(@Param("authorId") int authorId);
    
    /**
     * 保存文章
     *
     * @param article 需要保存的文章
     */
    Integer save(Article article);
    
    /**
     * 保存文章分类
     *
     * @param type
     */
    void saveType(ArticleType type);
    
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
     *  @param id
     * @param title
     * @param type
     */
    void updateArticle(@Param("id") Integer id, @Param("title") String title, @Param("type") Integer type,
                       @Param("forumId") Integer forumId);
    
    /**
     * 按ReadCount及CreateDate排序返回前指定个数的文章
     *
     * @param i 需要返回的文章数
     * @return 获取的文章清单，如果无则返回空的List
     */
    List<Article> findTopArticles(int i);
    
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
    List<Article> findByTopic(@Param("id") Integer id);
    
    /**
     * 通过标题关键字搜索文章
     *
     * @param key
     * @return
     */
    List<Article> searchByTitleKey(@Param("key") String key);
    
    /**
     * 修改文件是否置顶
     *
     * @param id
     * @param fixTop
     */
    void updateFixTop(@Param("id") Integer id, @Param("fixTop") boolean fixTop);
    
    /**
     * 更新文章是否推荐
     *
     * @param id
     * @param isRecommended
     */
    void updateRecommended(@Param("id") Integer id, @Param("recommended") boolean isRecommended);
}
