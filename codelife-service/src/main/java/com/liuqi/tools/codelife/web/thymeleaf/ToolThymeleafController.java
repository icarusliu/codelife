package com.liuqi.tools.codelife.web.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 工具Thymeleaf控制器
 *
 * @author LiuQI 2019/4/12 16:59
 * @version V1.0
 **/
@Controller
@RequestMapping("/tool")
public class ToolThymeleafController {
    @RequestMapping("/explorer")
    public String explorer() {
        return "tools";
    }
}
