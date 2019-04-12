package com.liuqi.tools.codelife.web.rest;

import com.liuqi.commons.web.rest.BaseEntityController;
import com.liuqi.tools.codelife.service.ToolService;
import com.liuqi.tools.codelife.service.dto.ToolVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工具控制器
 *
 * @author LiuQI 2019/4/12 16:58
 * @version V1.0
 **/
@RestController
@RequestMapping("/tool")
public class ToolController extends BaseEntityController<ToolVO, ToolService> {
    public ToolController(ToolService entityService) {
        super(entityService);
    }
}
