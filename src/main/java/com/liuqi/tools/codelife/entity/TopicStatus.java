package com.liuqi.tools.codelife.entity;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 专题状态
 */
public enum TopicStatus {
    WAIT_APPROVE("待审批"),
    NORMAL("正常");
    
    private String name;
    
    private TopicStatus(String name) {
        this.name = name;
    }
    
    @JsonValue
    public String getName() {
        return name;
    }
    
    public String toString() {
        return name;
    }
}
