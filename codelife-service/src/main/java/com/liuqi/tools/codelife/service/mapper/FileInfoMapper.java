package com.liuqi.tools.codelife.service.mapper;

import com.liuqi.commons.service.EntityMapper;
import com.liuqi.tools.codelife.db.entity.FileInfo;
import com.liuqi.tools.codelife.service.dto.FileInfoVO;
import org.mapstruct.Mapper;

/**
 * 文件对象转换器
 *
 * @author LiuQI 2019/4/10 18:22
 * @version V1.0
 **/
@Mapper(componentModel = "spring")
public interface FileInfoMapper extends EntityMapper<FileInfo, FileInfoVO> {
}
