package com.liuqi.tools.codelife.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LiuQI
 * @Created: 2019/1/29 21:33
 * @Version: V1.0
 **/
@Component
public class RestOrNotHandlerInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            boolean isRestRequest = method.hasMethodAnnotation(ResponseBody.class);
            isRestRequest = isRestRequest || !method.getMethod().getReturnType().equals(ModelAndView.class);
            isRestRequest = isRestRequest || method.getBeanType().isAnnotationPresent(RestController.class);
            request.setAttribute("isRestRequest", isRestRequest);
        }
        return true;
    }
}
