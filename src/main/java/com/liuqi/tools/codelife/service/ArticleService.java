package com.liuqi.tools.codelife.service;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;

import java.util.List;

/**
 * 文章服务类
 *
 * @Author: LiuQI
 * @Created: 2018/3/26 17:25
 * @Version: V1.0
 **/
public interface ArticleService {
    /**
     * 查找所有文章
     *
     * @return
     * @param nowPage
     * @param pageSize
     */
    PageInfo<Article> findAll(int nowPage, int pageSize);
    
    /**
     * 通过ID查找文章内容并返回
     * @param id 文章ID
     * @return 文章对象
     */
    Article findById(int id) throws RestException;
    

    
    /**
     * 保存文章
     *  @param title 文章标题
     * @param content 文章内容
     * @param type 文章类型
     * @param topicId
     */
    void saveArticle(String title, String content, int type, Integer topicId, Integer forumId) throws RestException;
    
    /**
     * 通过类型查找类型下的所有文章
     *
     *
     * @param id
     * @param nowPage
     * @return 该分类下的所有文章
     */
    PageInfo<Article> findByForum(Integer id, int nowPage, Integer pageSize);
    
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
     *  @param id
     * @param title
     * @param content
     * @param type
     * @param forumId
     */
    void updateArticle(Integer id, String title, String content, Integer type, Integer forumId) throws RestException;
    
    /**
     * 查找指定个数的文章并返回
     * 不包含文章内容，按ReadCount及CreateDate排序
     *
      * @param i 需要返回的文章个数
     * @return 返回获取的文章清单
     */
    List<Article> findTopArticleNoContent(int i);
    
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
     * 通过用户查找其所有文章清单
     *
     * @param user
     * @param typeId
     * @return
     */
    PageInfo<Article> findByAuthor(User user, int nowPage, int pageSize, Integer typeId) throws RestException;
    
    /**
     * 通过标题关键字进行搜索
     *
     * @param key
     * @return
     */
    PageInfo<Article> searchTitle(String key, int nowPage, int pageSize);
    
    /**
     * 文章置顶
     *
     * @param id
     */
    void fixTop(Integer id);
    
    /**
     *
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
}
