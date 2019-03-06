package com.liuqi.tools.codelife.config.security;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

/**
 * @Author: LiuQI
 * @Created: 2018/4/9 23:13
 * @Version: V1.0
 **/
public class AppVoter implements AccessDecisionVoter {
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }
    
    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {
        FilterInvocation invocation = (FilterInvocation) object;
        
        
        return ACCESS_GRANTED;
    }
    
    @Override
    public boolean supports(Class clazz) {
        return clazz.equals(FilterInvocation.class);
    }
}
