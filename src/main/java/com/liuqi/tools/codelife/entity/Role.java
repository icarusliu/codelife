package com.liuqi.tools.codelife.entity;

import java.io.Serializable;

/**
 * 角色对象
 *
 * @Author: LiuQI
 * @Created: 2018/3/24 15:48
 * @Version: V1.0
 **/
public class Role implements Serializable {
    private int id;
    private String name;
    /**
     * 备注信息
     */
    private String remark;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (null == obj || !(obj instanceof Role)) {
            return false;
        } else {
            Role destRole = (Role)obj;
            return destRole.getId() == getId();
        }
    }
}
