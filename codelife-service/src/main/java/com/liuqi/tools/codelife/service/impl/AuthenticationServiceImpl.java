package com.liuqi.tools.codelife.service.impl;

import com.liuqi.tools.codelife.config.security.RealUserDetails;
import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.service.AuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

/**
 * @Author: LiuQI
 * @Created: 2018/3/27 19:14
 * @Version: V1.0
 **/
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public User getLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object obj = authentication.getPrincipal();

        if (obj instanceof RealUserDetails) {
            RealUserDetails userDetails = (RealUserDetails) obj;
            return userDetails.getUser();
        }
        
        return null;
    }
    
    /**
     * 获取登录用户的IP
     *
     * @return
     */
    @Override
    public String getLoginUserIp() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Object obj = authentication.getDetails();
    
        if (obj instanceof RealUserDetails) {
            RealUserDetails userDetails = (RealUserDetails) obj;
            return userDetails.getRemoteIp();
        }

        if (obj instanceof OAuth2AuthenticationDetails) {
            return ((OAuth2AuthenticationDetails)obj).getRemoteAddress();
        }
        
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        return details.getRemoteAddress();
    }
}
