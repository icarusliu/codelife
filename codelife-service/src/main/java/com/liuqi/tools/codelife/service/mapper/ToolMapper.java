package com.liuqi.tools.codelife.service.mapper;

import com.liuqi.commons.service.EntityMapper;
import com.liuqi.tools.codelife.db.entity.Tool;
import com.liuqi.tools.codelife.service.dto.ToolVO;
import org.mapstruct.Mapper;

/**
 * .
 *
 * @author LiuQI 2019/4/12 16:56
 * @version V1.0
 **/
@Mapper(componentModel = "spring")
public interface ToolMapper extends EntityMapper<Tool, ToolVO> {
}
