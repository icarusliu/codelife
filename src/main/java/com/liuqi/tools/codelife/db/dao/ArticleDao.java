package com.liuqi.tools.codelife.db.dao;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.UserArticleStatInfo;
import com.liuqi.tools.codelife.exceptions.RestException;
import org.apache.ibatis.annotations.Param;

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
     * 查找文章，用于首页及文章浏览页展示用
     * 按系统是否推荐-发布时间进行排序
     *
     * @param forumId 版块编号，可以为空
     * @return
     */
    List<Article> findForExplorerOrderByRecommended(Integer forumId);
    
    /**
     * 查找文章，用于个人主页展示使用
     * 按以下顺序排序：个人是否置顶-发布时间
     *
     * @param authorId 不能为空
     * @param typeId 可以为空
     * @return
     */
    List<Article> findForExplorerOrderByFixTop(Integer authorId,
                                               Integer typeId);
    
    /**
     * 查找文章，用于管理使用，严格按发表时间排序
     *
     * @param authorId 用户编号，可以为空
     * @param typeId 分类编号，可以为空
     * @return
     */
    List<Article> findForManager(Integer authorId,
                                 Integer typeId);
    
    /**
     * 获取专题下的文章，用于浏览，按是否置顶等进行排序
     *
     * @param topicId 专题编号 不能为空
     * @return
     */
    List<Article> findByTopicForExplorer(Integer topicId);
    
    /**
     * 获取专题下的文章，用于管理，严格按发布时间排序
     * @param topicId
     * @return
     */
    List<Article> findByTopicForManager(Integer topicId);
    
    /**
     * 通过关键字搜索文章
     *
     * @param key
     * @return
     */
    List<Article> search(String key);
    
    /**
     * 查找文章
     * @param id
     * @throws RestException 当指定编号的文章不存在时抛出异常
     * @return 返回查找的文章
     */
    Article findById(int id) throws RestException;
    
    /**
     * 保存文章对象
     *
     * @param article 需要保存的文章对象
     */
    Integer save(Article article);
    
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
     * 为文章的点赞数增加一个值
     * 这个值可以是1或者-1，1时为点赞、-1时为取消点赞
     *
     * @param id 文章ID
     * @param i 变动的点赞数，可以是1或者是-1
     */
    void addPraiseCount(int id, int i);
    
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
    
    /**
     * 根据用户统计相关数据
     * 包含：用户文章的阅读总数、用户文章的点赞数
     * @param authorId
     * @return
     */
    UserArticleStatInfo getStatisticInfoByAuthor(int authorId);
}
