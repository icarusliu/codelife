package com.liuqi.tools.codelife.config.security;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @Author: LiuQI
 * @Created: 2018/4/9 23:13
 * @Version: V1.0
 **/
@Component
public class AppVoter implements AccessDecisionVoter {
    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }
    
    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {
        FilterInvocation invocation = (FilterInvocation) object;
        try {
            HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(invocation.getRequest());
            HandlerMethod handlerMethod = (HandlerMethod) handlerExecutionChain.getHandler();
            if (!handlerMethod.getBeanType().isAnnotationPresent(PreAuthorize.class)
                    && !handlerMethod.getMethod().isAnnotationPresent(PreAuthorize.class)) {
                return ACCESS_GRANTED;
            } else if (!authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                return ACCESS_DENIED;
            }

            boolean result = false;
            if (handlerMethod.getBeanType().isAnnotationPresent(PreAuthorize.class)) {
                PreAuthorize preAuthorize = handlerMethod.getBeanType().getAnnotation(PreAuthorize.class);
                String value = preAuthorize.value();
                if (value.contains("or")) {
                    for (String str : value.split("or")) {
                        if (str.contains("isAuthenticated")) {
                            result = true;
                            break;
                        }
                    }
                } else {
                    if (value.contains("isAuthenticated")) {

                    }

                    if (value.contains("hasAuthority('ADMIN')")) {

                    }

                    if (value.contains("hasAuthority('TOPIC_ADMIN')")) {

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ACCESS_GRANTED;
        }

        return ACCESS_DENIED;
    }
    
    @Override
    public boolean supports(Class clazz) {
        return clazz.equals(FilterInvocation.class);
    }
}
