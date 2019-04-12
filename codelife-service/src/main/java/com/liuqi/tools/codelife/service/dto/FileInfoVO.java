package com.liuqi.tools.codelife.service.dto;

import java.time.LocalDateTime;

/**
 * 文件对象
 *
 * @author LiuQI 2019/4/10 18:21
 * @version V1.0
 **/
public class FileInfoVO {
    private Integer Id;

    private String name;

    private String path;

    private String module;

    private Integer itemId;

    private LocalDateTime uploadTime;

    private Integer uploadUser;

    private String uploadDir;

    public Integer getId() {
        return Id;
    }

    public FileInfoVO setId(Integer id) {
        Id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FileInfoVO setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public FileInfoVO setPath(String path) {
        this.path = path;
        return this;
    }

    public String getModule() {
        return module;
    }

    public FileInfoVO setModule(String module) {
        this.module = module;
        return this;
    }

    public Integer getItemId() {
        return itemId;
    }

    public FileInfoVO setItemId(Integer itemId) {
        this.itemId = itemId;
        return this;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public FileInfoVO setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
        return this;
    }

    public Integer getUploadUser() {
        return uploadUser;
    }

    public FileInfoVO setUploadUser(Integer uploadUser) {
        this.uploadUser = uploadUser;
        return this;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public FileInfoVO setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
        return this;
    }

    @Override
    public String toString() {
        return "FileInfoVO{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", module='" + module + '\'' +
                ", itemId=" + itemId +
                ", uploadTime=" + uploadTime +
                ", uploadUser=" + uploadUser +
                ", uploadDir='" + uploadDir + '\'' +
                '}';
    }
}
