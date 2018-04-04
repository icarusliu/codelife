package com.liuqi.tools.codelife.entity;

/**
 * 用户状态枚举类
 * 状态包含审批中、正常、锁定及注释
 * 用户新注册时为审批中状态；通过审批后为正常状态；
 * 正常状态下如果一天超过3次输入错误将被锁定，第二天解锁后正常；
 * 账户如存在其它异常被手动注销后为注销状态；
 *
 */
public enum UserStatus {
    APPROVING("审批中"),
    NORMAL("正常"),
    LOCKED("锁定"),
    CANCEL("注销")
    ;
    private String name;
    
    private UserStatus(String name) {
        this.name = name;
    }
    
    /**
     * 返回状态名称
     *
     * @return 状态名称
     */
    public String getName() {
        return name;
    }
    
    /**
     *
     * @return
     */
    public String toString() {
        return name;
    }
}
