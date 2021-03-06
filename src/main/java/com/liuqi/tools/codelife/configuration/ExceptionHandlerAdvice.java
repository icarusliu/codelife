package com.liuqi.tools.codelife.configuration;

import com.liuqi.tools.codelife.exceptions.CommonException;
import com.liuqi.tools.codelife.exceptions.RestException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LiuQI
 * @Created: 2018/3/17 20:23
 * @Version: V1.0
 **/
@ControllerAdvice
public class ExceptionHandlerAdvice {
    /**
     * 处理返回页面的异常
     *
     * @param request
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(CommonException.class)
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ModelAndView view = new ModelAndView();
        view.addObject("exception", ex);
        view.addObject("url", request.getRequestURL());
        view.addObject("statusCode", response.getStatus());
        view.setViewName("error");
        return view;
    }
    
    /**
     * 处理Rest接口请求时的异常
     * @param request
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(RestException.class)
    @ResponseBody
    public Map<String, Object> restError(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("exception", ex);
        map.put("errorMessage", ex.getMessage());
        map.put("url", request.getRequestURL());
        map.put("statusCode", "500");
        return map;
    }
}
