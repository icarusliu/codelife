package com.liuqi.tools.codelife.db.entity;

import java.io.Serializable;

/**
 * 用户文章统计数据对象
 * 包含用户文章的总共阅读次数、合计点赞数等
 *
 * @Author: LiuQI
 * @Created: 2018/4/16 22:06
 * @Version: V1.0
 **/
public class UserArticleStatInfo implements Serializable{
    /**
     * 点赞次数
     */
    private int praiseCount;
    
    /**
     * 阅读次数
     */
    private int readCount;
    
    public int getPraiseCount() {
        return praiseCount;
    }
    
    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }
    
    public int getReadCount() {
        return readCount;
    }
    
    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}

