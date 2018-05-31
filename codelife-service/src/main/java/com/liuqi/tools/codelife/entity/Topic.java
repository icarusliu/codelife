package com.liuqi.tools.codelife.entity;

import java.io.Serializable;
import java.util.Collection;

/**
 * 专题对象
 *
 * @Author: LiuQI
 * @Created: 2018/4/2 17:03
 * @Version: V1.0
 **/
public class
Topic implements Serializable {
    private int id;
    
    /**
     * 专题名称
     */
    private String title;
    
    /**
     * 专题介绍
     */
    private String introduction;
    
    /**
     * 专题封面图片
     */
    private String img;
    
    private TopicStatus status;
    
    private TopicType type;
    
    private Collection<Article> articles;
    
    private User creator;
    
    private User admin;
    
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
    
    public String getIntroduction() {
        return introduction;
    }
    
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    
    public String getImg() {
        return img;
    }
    
    public void setImg(String img) {
        this.img = img;
    }
    
    public Collection<Article> getArticles() {
        return articles;
    }
    
    /**
     * 判断两个Topic对象是否是同一个
     * 如果ID一样则是同一个，否则不是
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Topic)) {
            return false;
        }
        
        int id = ((Topic)obj).getId();
        if (this.getId() == id) {
            return true;
        }
        
        return false;
    }
    
    public User getCreator() {
        return creator;
    }
    
    public void setCreator(User creator) {
        this.creator = creator;
    }
    
    public User getAdmin() {
        return admin;
    }
    
    public void setAdmin(User admin) {
        this.admin = admin;
    }
    
    public TopicStatus getStatus() {
        return status;
    }
    
    public void setStatus(TopicStatus status) {
        this.status = status;
    }
    
    public TopicType getType() {
        return type;
    }
    
    public void setType(TopicType type) {
        this.type = type;
    }
    
    public void setArticles(Collection<Article> articles) {
        this.articles = articles;
    }
}
