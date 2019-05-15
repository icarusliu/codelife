package com.liuqi.tools.codelife.db.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 文件信息对象
 *
 * @author LiuQI 2019/4/10 16:41
 * @version V1.0
 **/
@Entity
public class FileInfo {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name", columnDefinition = "varchar(64) comment '文件名称'")
    private String name;

    @Column(name = "path", columnDefinition = "varchar(64) comment '文件存储路径（包含文件名）'")
    private String path;

    @Column(name = "module", columnDefinition = "varchar(64) comment '文件所属模块'")
    private String module;

    @Column(name = "item_id", columnDefinition = "integer comment '文件所属对象主键'")
    private Integer itemId;

    @Column(name = "upload_time", columnDefinition = "timestamp default current_timestamp comment '文件上传时间'")
    private LocalDateTime uploadTime;

    @Column(name = "upload_user", columnDefinition = "integer comment '上传用户'")
    private Integer uploadUser;

    @Column(name = "download_count", columnDefinition = "integer default 0 comment '下载次数'")
    private Integer downloadCount;

    public Integer getId() {
        return id;
    }

    public FileInfo setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FileInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public FileInfo setPath(String path) {
        this.path = path;
        return this;
    }

    public String getModule() {
        return module;
    }

    public FileInfo setModule(String module) {
        this.module = module;
        return this;
    }

    public Integer getItemId() {
        return itemId;
    }

    public FileInfo setItemId(Integer itemId) {
        this.itemId = itemId;
        return this;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public FileInfo setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
        return this;
    }

    public Integer getUploadUser() {
        return uploadUser;
    }

    public FileInfo setUploadUser(Integer uploadUser) {
        this.uploadUser = uploadUser;
        return this;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public FileInfo setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
        return this;
    }
}
