package com.liuqi.tools.codelife.entity;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 专题类型
 * 0 开放式 1 封闭式
 * 开放式时所有人都可以看到 ；
 * 封闭式时浏览时无法看到；只有通过管理员添加才能加入专题
 */
public enum TopicType {
    OPEN("开放"),
    PROTECTED("封闭");
    
    private String name;
    
    private TopicType(String name) {
        this.name = name;
    }
    
    @JsonValue
    public String getName() {
        return name;
    }
    
    
    @Override
    public String toString() {
        return name;
    }
    
    public final Integer getOrdinal() {
        return this.ordinal();
    }
    
    public static TopicType valueOf(Integer ordinal) {
        if (ordinal == OPEN.ordinal()) {
            return OPEN;
        } else if (ordinal == PROTECTED.ordinal()) {
            return PROTECTED;
        }
        
        return OPEN;
    }
}
