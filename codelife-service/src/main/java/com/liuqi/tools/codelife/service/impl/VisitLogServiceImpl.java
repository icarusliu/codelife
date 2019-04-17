package com.liuqi.tools.codelife.service.impl;

import com.liuqi.commons.service.AbstractEntityService;
import com.liuqi.tools.codelife.db.entity.VisitLog;
import com.liuqi.tools.codelife.db.repository.VisitLogRepository;
import com.liuqi.tools.codelife.service.VisitLogService;
import com.liuqi.tools.codelife.service.dto.VisitLogVO;
import com.liuqi.tools.codelife.service.mapper.VisitLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 访问日志服务
 *
 * @author LiuQI 2019/3/6 15:45
 * @version V1.0
 **/
@Service
@Transactional
public class VisitLogServiceImpl extends AbstractEntityService<VisitLog, VisitLogVO,
        VisitLogRepository, VisitLogMapper> implements VisitLogService {
    public VisitLogServiceImpl(VisitLogRepository repository, VisitLogMapper mapper) {
        super(repository, mapper);
    }
}
