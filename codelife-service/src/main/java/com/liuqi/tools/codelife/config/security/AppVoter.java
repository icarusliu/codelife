package com.liuqi.tools.codelife.config.security;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(new DefaultWebSecurityExpressionHandler());

        FilterInvocation invocation = (FilterInvocation) object;
        List<ConfigAttribute> configAttributes = getAttributes(invocation);
        if (0 == configAttributes.size()) {
            return ACCESS_GRANTED;
        }
        return webExpressionVoter.vote(authentication, invocation, configAttributes);
    }

    public List<ConfigAttribute> getAttributes(FilterInvocation invocation) {
        try {
            HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(invocation.getRequest());
            HandlerMethod handlerMethod = (HandlerMethod) handlerExecutionChain.getHandler();
            if (!handlerMethod.getBeanType().isAnnotationPresent(PreAuthorize.class)
                    && !handlerMethod.getMethod().isAnnotationPresent(PreAuthorize.class)) {
                return new ArrayList<>(0);
            }

            List<ConfigAttribute> configAttributes = new ArrayList<>(16);
            PreAuthorize preAuthorize = handlerMethod.getBeanType().getAnnotation(PreAuthorize.class);
            if (null != preAuthorize) {
                String str = preAuthorize.value();
                ConfigAttribute configAttribute = new SecurityConfig(str);
                configAttributes.add(configAttribute);
            }

            preAuthorize = handlerMethod.getMethod().getAnnotation(PreAuthorize.class);
            if (null != preAuthorize) {
                String str = preAuthorize.value();
                ConfigAttribute configAttribute = new SecurityConfig(str);
                configAttributes.add(configAttribute);
            }

            return configAttributes;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(0);
        }
    }
    
    @Override
    public boolean supports(Class clazz) {
        return clazz.equals(FilterInvocation.class);
    }
}
