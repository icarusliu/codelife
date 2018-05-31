package com.liuqi.tools.codelife.controllers;

import com.liuqi.tools.codelife.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.exceptions.RestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * .
 *
 * @author LiuQI 2018/5/30 16:24
 * @version V1.0
 **/
@RestController
public class TestController {
    @GetMapping("/test1")
    public void test() throws RestException {
        throw ExceptionTool.getException(ErrorCodes.ARTICLE_CONTENT_NOT_EXISTS);
    }
}
