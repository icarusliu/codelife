package com.liuqi.tools.codelife.entity;

import java.io.Serializable;
import java.util.Collection;

/**
 * 用户对象
 *
 * @Author: LiuQI
 * @Created: 2018/3/23 21:56
 * @Version: V1.0
 **/
public class User implements Serializable{
    private int id;
    
    private String username;
    
    private String realName;
    
    private String password;
    
    private UserStatus status;
    
    private int errorCount;
    
    private String registerTime;
    
    public UserStatus getStatus() {
        return status;
    }
    
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    
    private Collection<Role> roles;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Collection<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
    
    public int getErrorCount() {
        return errorCount;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }
    
    /**
     * 用户是否有系统管理员权限
     *
     * @return
     */
    public boolean isSystemAdmin() {
        for (Role role: roles) {
            if (role.getId() == 1) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 是否是专题管理员
     *
     * @return
     */
    public boolean isTopicAdmin() {
        for (Role role: roles) {
            if ("TOPIC_ADMIN".equals(role.getName())) {
                return true;
            }
        }
        
        return false;
    }
    
    public String getRegisterTime() {
        return registerTime;
    }
    
    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
}
