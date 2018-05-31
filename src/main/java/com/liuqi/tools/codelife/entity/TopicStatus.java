package com.liuqi.tools.codelife.entity;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 专题状态
 * @author qi.liu
 */
public enum TopicStatus {
    /**
     * 待审批状态
     */
    WAIT_APPROVE("待审批"),

    /**
     * 正常状态
     */
    NORMAL("正常");
    
    private String name;
    
    TopicStatus(String name) {
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
}
