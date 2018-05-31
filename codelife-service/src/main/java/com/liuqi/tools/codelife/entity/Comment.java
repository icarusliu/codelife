package com.liuqi.tools.codelife.entity;

import java.io.Serializable;
import java.util.Collection;

/**
 * 评论对象
 *
 * @Author: LiuQI
 * @Created: 2018/4/13 15:57
 * @Version: V1.0
 **/
public class Comment implements Serializable{
    private Integer id;
    private String content;
    private CommentType type;
    private Integer destination;
    private String commentTime;
    private User commentUser;
    private String ip;
    private Boolean anonymos;
    
    private Collection<Comment> children;
    
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
    
    public CommentType getType() {
        return type;
    }
    
    public void setType(CommentType type) {
        this.type = type;
    }
    
    public Integer getDestination() {
        return destination;
    }
    
    public void setDestination(Integer destination) {
        this.destination = destination;
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
    
    public Collection<Comment> getChildren() {
        return children;
    }
    
    public void setChildren(Collection<Comment> children) {
        this.children = children;
    }
}
