package com.liuqi.tools.codelife.service;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.db.entity.Article;
import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.db.entity.UserArticleStatInfo;
import com.liuqi.tools.codelife.util.exceptions.RestException;

/**
 * 文章服务类
 *
 * @Author: LiuQI
 * @Created: 2018/3/26 17:25
 * @Version: V1.0
 **/
public interface ArticleService {
    /**
     * 查找所有文章，用于展现到首页或者是文章浏览模块中使用
     * 用于展现的文章清单需要按一些特定顺序排序，如是否置顶等；
     * @param nowPage
     * @param pageSize
     * @return
     */
    PageInfo<Article> findForExplorer(int nowPage, int pageSize);
    
    /**
     * 查找所有指定版块下的文章，用于展现到首页或者是文章浏览模块中使用
     * 用于展现的文章清单需要按一些特定顺序排序，如是否置顶等；
     *
     * @param forumId
     * @param nowPage
     * @param pageSize
     * @return
     */
    PageInfo<Article> findForExplorer(Integer forumId, int nowPage, int pageSize);
    
    /**
     * 根据用户查找其发表的所有文章，用于文章浏览使用
     * 按是否置顶等进行排序
     *
     * @param user
     * @param nowPage
     * @param pageSize
     * @return
     * @throws RestException
     */
    PageInfo<Article> findByAuthorForExplorer(User user, int nowPage, int pageSize) throws RestException;
    
    /**
     * 根据用户查找其某个分类下的所有文章，用于文章浏览使用
     * 按是否置顶等进行排序
     *
     * @param user
     * @param typeId
     * @param nowPage
     * @param pageSize
     * @return
     * @throws RestException
     */
    PageInfo<Article> findByAuthorForExplorer(User user, Integer typeId, int nowPage, int pageSize) throws RestException;
    
    /**
     * 查找文章，用于文章管理中使用
     * 其排序将严格按照发布时间来进行排序
     *
     * @param user 如果是管理员，则返回所有文章，否则返回该用户发表的文章
     * @param nowPage
     * @param pageSize
     * @return
     */
    PageInfo<Article> findForManager(User user, int nowPage, int pageSize);
    
    /**
     * 查找指定分类的文章，用于文章管理中使用
     * 其排序将严格按照发布时间来进行排序
     *
     * @param user 如果是管理员，则在所有文章中查找，否则只查找该用户发表的文章
     * @param typeId
     * @param nowPage
     * @param pageSize
     * @return
     */
    PageInfo<Article> findForManager(User user, Integer typeId, int nowPage, int pageSize);
    
    /**
     * 根据关键字搜索文章
     * 搜索的结果将按文章的优先级进行排序，如是否置顶等
     *
     * @param key
     * @param nowPage
     * @param pageSize
     * @return
     */
    PageInfo<Article> search(String key, int nowPage, int pageSize);
    
    /**
     * 通过ID查找文章内容并返回
     * @param id 文章ID
     * @return 文章对象
     * @throws RestException
     */
    Article findById(int id) throws RestException;
    
    /**
     * 保存文章
     *  @param title 文章标题
     * @param content 文章内容
     * @param type 文章类型
     * @param topicId 专题 可以为空
     * @param forumId 版块，可以为空，文章可以不发表到任何版块
     * @throws RestException
     */
    void saveArticle(String title, String content, Integer type, Integer topicId, Integer forumId) throws RestException;
    
    /**
     * 对应文章的阅读次数加1
     * @param article
     */
    void addReadCount(Article article);
    
    /**
     * 删除文章
     *
     * @param id
     */
    void deleteArticle(Integer id);
    
    /**
     * 更新文章
     *  @param id 文章编号，不能为空
     * @param title 文章标题，不能为空
     * @param content 文章内容，不能为空
     * @param type  文章分类，不能为空
     * @throws RestException
     */
    void updateArticle(Integer id, String title, String content, Integer type) throws RestException;
    
    /**
     * 对文章进行点赞
     *
     * @param id 需要点赞的文章
     */
    void praise(int id);
    
    /**
     * 对文章取消点赞
     *
     * @param id 文章ID
     */
    void unpraise(int id);
    
    /**
     * 文章置顶
     *
     * @param id
     */
    void fixTop(Integer id);
    
    /**
     * 取消置顶
     * @param id
     */
    void unFixTop(Integer id);
    
    /**
     * 文章推荐
     * 推荐的文章将会在首页以及文章浏览中显示在前面
     *
     * @param id
     */
    void recommend(Integer id);
    
    /**
     * 文章取消推荐
     * @param id
     */
    void unRecommend(Integer id);
    
    /**
     * 根据用户统计相关数据
     * 包含：用户文章的阅读总数、用户文章的点赞数
     * @param authorId
     * @return
     */
    UserArticleStatInfo getStatisticInfoByAuthor(int authorId);
}
