package com.liuqi.tools.codelife.db.entity;

/**
 * 评论类型
 * @author qi.liu
 */
public enum CommentType {
    /**
     * 文章评论
     */
    ARTICLE("文章评论"),

    /**
     * 专题评论
     */
    TOPIC("专题评论"),

    /**
     * 评论的评论
     */
    COMMENT("评论的评论")
    ;
    private String name;
    
    CommentType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
