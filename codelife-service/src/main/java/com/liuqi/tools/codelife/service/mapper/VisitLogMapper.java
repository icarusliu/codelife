package com.liuqi.tools.codelife.service.mapper;

import com.liuqi.commons.service.EntityMapper;
import com.liuqi.tools.codelife.db.entity.VisitLog;
import com.liuqi.tools.codelife.service.dto.VisitLogDTO;
import org.mapstruct.Mapper;

/**
 * 访问日志转换类
 *
 * @author LiuQI 2019/3/6 15:35
 * @version V1.0
 **/
@Mapper(componentModel = "spring")
public interface VisitLogMapper extends EntityMapper<VisitLog, VisitLogDTO> {

}
