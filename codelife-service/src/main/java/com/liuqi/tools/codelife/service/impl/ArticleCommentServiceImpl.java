package com.liuqi.tools.codelife.service.impl;

import com.liuqi.tools.codelife.db.dao.ArticleCommentDao;
import com.liuqi.tools.codelife.db.entity.ArticleComment;
import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.service.ArticleService;
import com.liuqi.tools.codelife.util.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.util.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleCommentService;
import com.liuqi.tools.codelife.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 评论服务实现类
 *
 * @Author: LiuQI
 * @Created: 2018/4/13 17:18
 * @Version: V1.0
 **/
@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {
    @Autowired
    private ArticleCommentDao articleCommentDao;

    @Resource
    private ArticleService articleService;
    
    /**
     * 添加评论
     * @param content
     * @param anonymos
     * @param loginUser
     * @param host
     * @param showName
     */
    @Override
    public void add(String content, Boolean anonymos, Integer articleId, Integer parentCommentId,
                    User loginUser, String host, String showName) throws RestException {
        if (null == content || "".equals(content.trim())) {
            logger.error("Content cannot be null or empty!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "评论内容");
        }
        
        if (null == articleId) {
            logger.error("Article cannot be null!");
            throw ExceptionTool.getException(ErrorCodes.COMM_PARAMETER_EMPTY, "文章编号");
        }
        
        if (null == host) {
            host = "";
        }
        
        ArticleComment articleComment = new ArticleComment();
        articleComment.setCommentTime(DateUtils.getNowDateStr());
        articleComment.setCommentUser(loginUser);
        articleComment.setAnonymos(anonymos);
        articleComment.setContent(content.trim());
        articleComment.setIp(host.trim());
        articleComment.setShowName(showName);
        if (null != parentCommentId) {
            articleComment.setParent(articleCommentDao.findById(parentCommentId));
        }
        articleComment.setArticle(articleService.findById(articleId));

        articleCommentDao.add(articleComment);
    }
    
    @Override
    public Collection<ArticleComment> findByArticle(Integer articleId) {
        return articleCommentDao.findByArticle(articleId);
    }
    
    @Override
    public int getCommentCountByAuthor(Integer authorId) {
        return articleCommentDao.getCommentCountByAuthor(authorId);
    }

    /**
     * 获取新增加的评论列表
     *
     * @param count 需要查询的评论数目
     * @return 新增加的评论列表
     */
    @Override
    public List<ArticleComment> findLatestComments(int count) {
        List<ArticleComment> articleCommentList = articleCommentDao.findNewerComments(count);


        return articleCommentList;
    }

    private static final Logger logger = LoggerFactory.getLogger(ArticleCommentServiceImpl.class);
}
