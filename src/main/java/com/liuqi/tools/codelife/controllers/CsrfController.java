package com.liuqi.tools.codelife.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: LiuQI
 * @Created: 2018/3/24 23:45
 * @Version: V1.0
 **/
@RestController
public class CsrfController {
    
    @RequestMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
}
