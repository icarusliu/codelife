package com.liuqi.tools.codelife.db.entity;

/**
 * @Author: LiuQI
 * @Created: 2018/3/26 16:19
 * @Version: V1.0
 **/
public enum ArticleStatus {
    /**
     * 审批中
     */
    APPROVING("审批中"),
    APPROVED("审批通过"),
    INAPPROVED("审批未通过"),
    NON_VISIBLE("隐藏"),
    DRAFT("草稿");
    
    private String name;
    
    private ArticleStatus(String name) {
        this.name = name;
    }

    public static ArticleStatus parse(Integer original) {
        for (ArticleStatus articleStatus : ArticleStatus.values()) {
            if (articleStatus.ordinal() == original) {
                return articleStatus;
            }
        }

        return APPROVED;
    }
}
