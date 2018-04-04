package com.liuqi.tools.codelife.configuration.unuse;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LiuQI
 * @Created: 2018/3/17 20:32
 * @Version: V1.0
 **/
//@Component
public class CommonHandlerExceptionResolver implements HandlerExceptionResolver {
    @Nullable
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
        ModelAndView view = new ModelAndView();
        view.addObject("exception", ex);
        view.addObject("url", request.getRequestURL());
        view.addObject("statusCode", response.getStatus());
        view.setViewName("error");
        return view;
    }
}
