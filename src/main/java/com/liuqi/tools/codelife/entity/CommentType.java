package com.liuqi.tools.codelife.entity;

/**
 * 评论类型
 */
public enum CommentType {
    ARTICLE("文章评论"),
    TOPIC("专题评论"),
    COMMENT("评论的评论")
    ;
    private String name;
    
    private CommentType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
