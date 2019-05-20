package com.liuqi.tools.codelife.service.impl;

import com.github.pagehelper.PageInfo;
import com.liuqi.tools.codelife.db.dao.ArticleDao;
import com.liuqi.tools.codelife.db.entity.*;
import com.liuqi.tools.codelife.util.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.util.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import com.liuqi.tools.codelife.service.*;
import com.liuqi.tools.codelife.util.ArticleBuilder;
import com.liuqi.tools.codelife.util.ArticleUtils;
import com.liuqi.tools.codelife.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 文章服务类的实现类
 *
 * @Author: LiuQI
 * @Created: 2018/3/26 17:26
 * @Version: V1.0
 **/
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleDao articleDao;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private TopicService topicService;
    
    @Value("${app.file.savePath}")
    private String contentFilePath;

    @Autowired
    private ArticleTypeService articleTypeService;

    @Autowired
    private FileInfoService fileInfoService;
    
    @Override
    public Article findById(int id) throws RestException {
        Article article = articleDao.findById(id);
        article.setContent(FileUtils.getFileContent(article.getContentUrl(), contentFilePath));
        
        return article;
    }
    
    /**
     * 查找文章用于展现用
     * 按是否置顶等顺序进行排序
     *
     * @param nowPage
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Article> findForExplorer(int nowPage, int pageSize) {
        return PageInfo.of(articleDao.findForExplorerOrderByRecommended(null));
    }
    
    @Override
    public PageInfo<Article> findForExplorer(Integer forumId, int nowPage, int pageSize) {
        return PageInfo.of(articleDao.findForExplorerOrderByRecommended(forumId));
    }
    
    @Override
    public PageInfo<Article> findByAuthorForExplorer(User user, int nowPage, int pageSize) throws RestException {
        if (null == user) {
            logger.error("User cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "作者");
        }
        return PageInfo.of(articleDao.findForExplorerOrderByFixTop(user.getId(), null));
    }
    
    @Override
    public PageInfo<Article> findByAuthorForExplorer(User user, Integer typeId, int nowPage, int pageSize) throws RestException {
        if (null == user) {
            logger.error("User cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "作者");
        }
        
        return PageInfo.of(articleDao.findForExplorerOrderByFixTop(user.getId(), typeId));
    }
    
    @Override
    public PageInfo<Article> findForManager(User user, int nowPage, int pageSize) {
        if (null != user && user.isSystemAdmin()) {
            //如果是系统管理员，则能看到所有文章
            user = null;
        }
        
        return PageInfo.of(articleDao.findForManager(null == user ? null : user.getId(), null));
    }
    
    @Override
    public PageInfo<Article> findForManager(User user, Integer typeId, int nowPage, int pageSize) {
        if (null != user && user.isSystemAdmin()) {
            //如果是系统管理员，则能看到所有文章
            user = null;
        }
        
        return PageInfo.of(articleDao.findForManager(null == user ? null : user.getId(), typeId));
    }
    
    @Override
    public PageInfo<Article> search(String key, int nowPage, int pageSize) {
        if (null == key) {
            return new PageInfo(Collections.EMPTY_LIST);
        }
        return new PageInfo(articleDao.search(key));
    }
    
    @Override
    public void saveArticle(Article article, List<Integer> fileIds) throws RestException {
        article.setRemark(article.getContent());
        Integer articleId = articleDao.save(article);
        
        //文章分类对象中文章数目加1
        if (null != article.getForum()) {
            articleTypeService.addArticleCount(article.getForum().getId());
        }
        articleTypeService.addArticleCount(article.getArticleType().getId());
        
        // 绑定文件与文章的关系
        if (!CollectionUtils.isEmpty(fileIds)) {
            fileInfoService.batchUpdateItemId(fileIds, articleId);
        }
    }
    
    @Override
    public void addReadCount(Article article) {
        articleDao.addReadCount(article.getId());
    }
    
    /**
     * 根据ID删除文章
     *
     * @param id
     */
    @Override
    public void deleteArticle(Integer id) {
        // 如果文章已经加入专题，需要先将文章从专题中删除
        topicService.deleteTopicArticle(id);

        articleDao.deleteArticle(id);
        
        //分类中文章数目减1
        articleTypeService.deduceArticleCount(id);
    }
    
    /**
     * 更新文章
     * @param fileIds   附件文件编号清单
     */
    @Override
    public void updateArticle(Article article, List<Integer> fileIds, Integer oldForumId, Integer oldType) throws RestException {
        //更新数据库信息 版块不进行更新
        articleDao.updateArticle(article);
        
        //更新分类中的文章计数信息
        if (oldType != article.getArticleType().getId()) {
            articleTypeService.addArticleCount(article.getArticleType().getId());
            articleTypeService.deduceArticleCount(oldType);
        }

        // 更新版本中的文章计数信息
        if (!oldForumId.equals(article.getForum().getId())) {
            articleTypeService.addArticleCount(article.getForum().getId());
            articleTypeService.deduceArticleCount(oldForumId);
        }

        // 更新文章附件
        if (!CollectionUtils.isEmpty(fileIds)) {
            fileInfoService.batchUpdateItemId(fileIds, article.getId());
        }
    }
    
    @Override
    public void praise(int id) {
        //不检查文章是否存在
        //更新失败对用户无影响
        articleDao.addPraiseCount(id, 1);
    }
    
    @Override
    public void unpraise(int id) {
        //不检查文章是否存在
        //更新失败对用户无影响
        articleDao.addPraiseCount(id, -1);
    }
    
    @Override
    public void fixTop(Integer id) {
        articleDao.updateFixTop(id, true);
    }
    
    @Override
    public void unFixTop(Integer id) {
        articleDao.updateFixTop(id, false);
    }
    
    @Override
    public void recommend(Integer id) {
        articleDao.updateRecommended(id, true);
    }
    
    @Override
    public void unRecommend(Integer id) {
        articleDao.updateRecommended(id, false);
    }
    
    @Override
    public UserArticleStatInfo getStatisticInfoByAuthor(int authorId) {
        return articleDao.getStatisticInfoByAuthor(authorId);
    }

    /**
     * 查询热闹文章
     *
     * @return 热闹文章清单
     */
    @Override
    public PageInfo<Article> findHotArticles(int nowPage, int pageSize) {
        return PageInfo.of(articleDao.findHotArticles());
    }

    /**
     * 查找不在专题中的文章清单
     *
     * @param topicId 专题编号
     * @param key     关键字
     * @return 不在指定专题中的文章清单
     */
    @Override
    public PageInfo<Article> findNotInTopic(Integer topicId, String key, int nowPage, int pageSize) {
        return PageInfo.of(articleDao.findNotInTopic(topicId, key));
    }

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
}
