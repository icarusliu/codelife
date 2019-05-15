package com.liuqi.tools.codelife.service.dto;

import com.liuqi.tools.codelife.db.entity.FileInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 工具实体视图对象
 *
 * @author LiuQI 2019/4/12 16:54
 * @version V1.0
 **/
public class ToolVO {
    private Integer id;

    private String name;

    private String desc;

    /**
     * 附件信息
     */
    private FileInfo fileInfo;

    /**
     * 图片信息
     */
    private List<FileInfo> images;

    private LocalDateTime updateTime;

    private Integer downloadCount;

    public Integer getId() {
        return id;
    }

    public ToolVO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ToolVO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public ToolVO setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public ToolVO setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
        return this;
    }

    public List<FileInfo> getImages() {
        return images;
    }

    public ToolVO setImages(List<FileInfo> images) {
        this.images = images;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public ToolVO setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public ToolVO setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
        return this;
    }

    @Override
    public String toString() {
        return "ToolVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", fileInfo=" + fileInfo +
                ", images=" + images +
                ", updateTime=" + updateTime +
                ", downloadCount=" + downloadCount +
                '}';
    }
}
