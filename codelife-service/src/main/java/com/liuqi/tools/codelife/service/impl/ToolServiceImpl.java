package com.liuqi.tools.codelife.service.impl;

import com.liuqi.commons.service.AbstractEntityService;
import com.liuqi.tools.codelife.db.entity.Tool;
import com.liuqi.tools.codelife.db.repository.ToolRepository;
import com.liuqi.tools.codelife.service.ToolService;
import com.liuqi.tools.codelife.service.dto.ToolVO;
import com.liuqi.tools.codelife.service.mapper.ToolMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工具服务
 *
 * @author LiuQI 2019/4/12 16:57
 * @version V1.0
 **/
@Service
@Transactional
public class ToolServiceImpl
        extends AbstractEntityService<Tool, ToolVO, ToolRepository, ToolMapper>
        implements ToolService {
    public ToolServiceImpl(ToolRepository repository, ToolMapper mapper) {
        super(repository, mapper);
    }
}
