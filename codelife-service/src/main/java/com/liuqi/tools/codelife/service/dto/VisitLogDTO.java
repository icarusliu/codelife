package com.liuqi.tools.codelife.service.dto;

import java.time.LocalDateTime;

/**
 * 访问日志对象
 *
 * @author LiuQI 2019/3/6 15:33
 * @version V1.0
 **/
public class VisitLogDTO {
    private Long id;

    private String userIp;

    private String requestUrl;

    private LocalDateTime visitTime;

    private String user;

    private String params;

    public Long getId() {
        return id;
    }

    public VisitLogDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUserIp() {
        return userIp;
    }

    public VisitLogDTO setUserIp(String userIp) {
        this.userIp = userIp;
        return this;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public VisitLogDTO setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }

    public LocalDateTime getVisitTime() {
        return visitTime;
    }

    public VisitLogDTO setVisitTime(LocalDateTime visitTime) {
        this.visitTime = visitTime;
        return this;
    }

    public String getUser() {
        return user;
    }

    public VisitLogDTO setUser(String user) {
        this.user = user;
        return this;
    }

    public String getParams() {
        return params;
    }

    public VisitLogDTO setParams(String params) {
        this.params = params;
        return this;
    }
}
