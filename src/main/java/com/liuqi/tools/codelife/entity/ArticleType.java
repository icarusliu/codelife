package com.liuqi.tools.codelife.entity;

/**
 * 文章分类
 * 每个用户一个文章分类
 * 分类可以由每个用户单独创建
 *
 * @Author: LiuQI
 * @Created: 2018/3/26 16:16
 * @Version: V1.0
 **/
public class ArticleType {
    private int id;
    private int userId;
    private String name;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
