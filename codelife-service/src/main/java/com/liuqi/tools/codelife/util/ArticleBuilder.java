package com.liuqi.tools.codelife.util;

import com.liuqi.tools.codelife.entity.Article;
import com.liuqi.tools.codelife.entity.ArticleStatus;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文章对象的构造器
 *
 * @Author: LiuQI
 * @Created: 2018/3/27 8:33
 * @Version: V1.0
 **/
public final class ArticleBuilder {
    /**
     * 防止外部实例化
     */
    private ArticleBuilder() {}
    
    private Article article;
    
    /**
     * 获取构造器对象
     * 默认为文章添加以下属性：
     * 创建时间、用户、状态、读取次数
     *
     * @param title 文章标题
     * @return 返回构造器对象
     */
    public static ArticleBuilder of(String title) {
        ArticleBuilder builder = new ArticleBuilder();
        builder.article = new Article();
        builder.article.setTitle(title);
        builder.article.setReadCount(0);
        builder.article.setStatus(ArticleStatus.APPROVING);
        builder.article.setCreateDate(DateUtils.getNowDateStr());
        
        return builder;
    }
    
    /**
     * 设置文章内容
     * 文件内容保存到独立的文件中去；
     * 最终数据库中保存的是文件路径;
     *
     * @param content HTML内容
     * @param contentFilePath 文件存储路径
     * @return 返回Builder对象
     */
    public ArticleBuilder setContent(String content, String contentFilePath) throws RestException {
        String fileName = FileUtils.saveToFile(content, contentFilePath);
        article.setContentUrl(fileName);
        
        String html = FileUtils.htmlToText(content);
        article.setRemark(html.substring(0, Math.min(200, html.length())));
        
        return this;
    }
    
    /**
     * 设置文章类型
     *
     * @param type
     * @return
     */
    public ArticleBuilder setType(ArticleType type) {
        article.setArticleType(type);
        return this;
    }
    
    /**
     * 设置文章作者
     *
     * @param user
     * @return
     */
    public ArticleBuilder setAuthor(User user) {
        article.setAuthorID(user.getId());
        article.setAuthorName(user.getUsername());
        return this;
    }
    
    /**
     * 设置文章所在版块
     * @param forum
     * @return
     */
    public ArticleBuilder setForum(ArticleType forum) {
        article.setForum(forum);
        return this;
    }
    
    /**
     * 生成文章对象
     *
     * @return
     */
    public Article build() {
        return article;
    }
    
    
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleBuilder.class);
}
