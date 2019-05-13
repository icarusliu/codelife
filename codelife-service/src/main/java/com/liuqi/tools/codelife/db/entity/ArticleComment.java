package com.liuqi.tools.codelife.db.entity;

import java.io.Serializable;
import java.util.Collection;

/**
 * 评论对象
 *
 * @Author: LiuQI
 * @Created: 2018/4/13 15:57
 * @Version: V1.0
 **/
public class ArticleComment implements Serializable{
    private Integer id;
    private String content;
    private Article article;
    private ArticleComment parent;
    private String commentTime;
    private User commentUser;
    private String ip;
    private Boolean anonymos;

    private Collection<ArticleComment> children;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    public Article getArticle() {
        return article;
    }

    public ArticleComment setArticle(Article article) {
        this.article = article;
        return this;
    }

    public ArticleComment getParent() {
        return parent;
    }

    public ArticleComment setParent(ArticleComment parent) {
        this.parent = parent;
        return this;
    }

    public String getCommentTime() {
        return commentTime;
    }
    
    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }
    
    public User getCommentUser() {
        return commentUser;
    }
    
    public void setCommentUser(User commentUser) {
        this.commentUser = commentUser;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public Boolean getAnonymos() {
        return anonymos;
    }
    
    public void setAnonymos(Boolean anonymos) {
        this.anonymos = anonymos;
    }
    
    public Collection<ArticleComment> getChildren() {
        return children;
    }
    
    public void setChildren(Collection<ArticleComment> children) {
        this.children = children;
    }
}
