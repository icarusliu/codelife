package com.liuqi.tools.codelife.db.repository;

import com.liuqi.commons.db.BaseRepository;
import com.liuqi.tools.codelife.db.entity.FileInfo;
import org.springframework.stereotype.Repository;

/**
 * 文件对象数据库操作类
 *
 * @author LiuQI 2019/4/10 18:19
 * @version V1.0
 **/
@Repository
public interface FileInfoRepository extends BaseRepository<FileInfo> {
}
