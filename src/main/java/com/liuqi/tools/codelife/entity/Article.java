package com.liuqi.tools.codelife.entity;

import java.io.Serializable;

/**
 * 文章对象
 *
 *
 * @Author: LiuQI
 * @Created: 2018/3/26 16:12
 * @Version: V1.0
 **/
public class
Article implements Serializable {
    private int id;
    private String title;
    private ArticleType articleType;
    private int authorID;
    private String authorName;
    private String createDate;
    private ArticleStatus status;
    private String contentUrl;
    private String content;
    private String remark;
    
    /**
     * 阅读次数
     */
    private int readCount;
    
    /**
     * 点赞数
     */
    private int praiseCount;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public ArticleType getArticleType() {
        return articleType;
    }
    
    public void setArticleType(ArticleType articleType) {
        this.articleType = articleType;
    }
    
    public int getAuthorID() {
        return authorID;
    }
    
    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }
    
    public String getAuthorName() {
        return authorName;
    }
    
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    public String getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    
    public ArticleStatus getStatus() {
        return status;
    }
    
    public void setStatus(ArticleStatus status) {
        this.status = status;
    }
    
    public String getContentUrl() {
        return contentUrl;
    }
    
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public int getReadCount() {
        return readCount;
    }
    
    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public int getPraiseCount() {
        return praiseCount;
    }
    
    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }
}
