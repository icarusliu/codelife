package com.liuqi.tools.codelife.db.entity;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 访问记录
 *
 * @author LiuQI 2019/3/6 14:56
 * @version V1.0
 **/
@Entity
public class VisitLog {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "user_id", columnDefinition = "varchar(255) comment '用户地址'")
    private String userIp;

    @Column(name = "request_url", columnDefinition = "varchar(255) comment '请求地址'")
    private String requestUrl;

    @Column(name = "visit_time", columnDefinition = "timestamp comment '访问时间'")
    private LocalDateTime visitTime;

    @Column(name = "user", columnDefinition = "varchar(255) comment '访问用户'")
    private String user;

    @Column(name = "params", columnDefinition = "varchar(1000) comment '请求参数'")
    private String params;

    public Integer getId() {
        return id;
    }

    public VisitLog setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUserIp() {
        return userIp;
    }

    public VisitLog setUserIp(String userIp) {
        this.userIp = userIp;
        return this;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public VisitLog setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }

    public LocalDateTime getVisitTime() {
        return visitTime;
    }

    public VisitLog setVisitTime(LocalDateTime visitTime) {
        this.visitTime = visitTime;
        return this;
    }

    public String getUser() {
        return user;
    }

    public VisitLog setUser(String user) {
        this.user = user;
        return this;
    }

    public String getParams() {
        return params;
    }

    public VisitLog setParams(String params) {
        if (StringUtils.isNotBlank(params)) {
            if (params.length() > 1000) {
                params = params.substring(0, 1000);
            }
        }
        this.params = params;
        return this;
    }
}
