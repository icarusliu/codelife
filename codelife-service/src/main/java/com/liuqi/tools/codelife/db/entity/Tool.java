package com.liuqi.tools.codelife.db.entity;

import javax.persistence.*;
import java.util.List;

/**
 * 常用工具
 *
 * @author LiuQI 2019/4/12 16:43
 * @version V1.0
 **/
@Entity
public class Tool {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name", columnDefinition = "varchar(64) comment '工具名称'")
    private String name;

    @Column(name = "desc", columnDefinition = "varchar(255) comment '工具描述'")
    private String desc;

    /**
     * 附件信息
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private FileInfo fileInfo;

    /**
     * 图片信息
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tool_image", joinColumns = {@JoinColumn(name = "tool_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "file_id", referencedColumnName = "id")})
    private List<FileInfo> images;

    public Integer getId() {
        return id;
    }

    public Tool setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Tool setName(String name) {
        this.name = name;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public Tool setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public Tool setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
        return this;
    }

    public List<FileInfo> getImages() {
        return images;
    }

    public Tool setImages(List<FileInfo> images) {
        this.images = images;
        return this;
    }
}
