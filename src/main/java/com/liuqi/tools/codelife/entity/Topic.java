package com.liuqi.tools.codelife.entity;

/**
 * 专题对象
 *
 * @Author: LiuQI
 * @Created: 2018/4/2 17:03
 * @Version: V1.0
 **/
public class Topic {
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
}
