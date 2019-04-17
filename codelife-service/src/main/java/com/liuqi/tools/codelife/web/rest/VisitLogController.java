package com.liuqi.tools.codelife.web.rest;

import com.liuqi.commons.web.rest.BaseEntityController;
import com.liuqi.tools.codelife.service.VisitLogService;
import com.liuqi.tools.codelife.service.dto.VisitLogVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 访问日志操作类
 *
 * @author LiuQI 2019/3/6 16:14
 * @version V1.0
 **/
@RestController
@RequestMapping("/visitLog")
@PreAuthorize("hasAuthority('ADMIN')")
public class VisitLogController extends BaseEntityController<VisitLogVO, VisitLogService> {

    public VisitLogController(VisitLogService entityService) {
        super(entityService);
    }
}
