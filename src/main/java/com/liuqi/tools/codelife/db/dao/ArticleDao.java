package com.liuqi.tools.codelife.db.dao;

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
public interface ArticleDao {
    /**
     * 查找文章，用于首页及文章浏览页展示用
     * 按系统是否推荐-发布时间进行排序
     *
     * @param forumId 版块编号，可以为空
     * @return
     */
    List<Article> findForExplorerOrderByRecommended(@Param("forumId") Integer forumId);
    
    /**
     * 查找文章，用于个人主页展示使用
     * 按以下顺序排序：个人是否置顶-发布时间
     *
     * @param authorId 不能为空
     * @param typeId 可以为空
     * @return
     */
    List<Article> findForExplorerOrderByFixTop(@Param("authorId") Integer authorId,
                                               @Param("typeId") Integer typeId);
    
    /**
     * 查找文章，用于管理使用，严格按发表时间排序
     *
     * @param authorId 用户编号，可以为空
     * @param typeId 分类编号，可以为空
     * @return
     */
    List<Article> findForManager(@Param("authorId") Integer authorId,
                                 @Param("typeId") Integer typeId);
    
    /**
     * 获取专题下的文章，用于浏览，按是否置顶等进行排序
     *
     * @param topicId 专题编号 不能为空
     * @return
     */
    List<Article> findByTopicForExplorer(@Param("topicId") Integer topicId);
    
    /**
     * 获取专题下的文章，用于管理，严格按发布时间排序
     * @param topicId
     * @return
     */
    List<Article> findByTopicForManager(@Param("topicId") Integer topicId);
    
    /**
     * 通过关键字搜索文章
     *
     * @param key
     * @return
     */
    List<Article> search(@Param("key") String key);
    
    /**
     * 通过编号查找文章
     *
     * @param id
     * @return
     */
    Article findById(@Param("id") Integer id);
    
    /**
     * 保存文章
     *
     * @param article 需要保存的文章
     * @return 返回新增的文章ID
     */
    Integer save(Article article);
    
    /**
     * 保存文章分类
     *
     * @param type 需要保存的文章分类
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
     * @param id
     * @param title
     * @param type
     * @param forumId
     */
    void updateArticle(@Param("id") Integer id, @Param("title") String title, @Param("type") Integer type,
                       @Param("forumId") Integer forumId);
    
    /**
     * 点赞
     * 当前用户是否对某篇文章点赞，存储在Cookie中，数据库中只存储点赞数
     *
     * @param id 点赞的编号
     * @param count 点赞次数，可以是1或者-1，-1时表示是取消点赞
     */
    void addPraiseCount(@Param("id") int id, @Param("count") int count);
    
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
    
    /**
     * 根据用户统计相关数据
     * 包含：用户文章的阅读总数、用户文章的点赞数
     * @param authorId
     * @return
     */
    UserArticleStatInfo getStatisticInfoByAuthor(@Param("authorId") int authorId);
}
