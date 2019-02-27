package com.liuqi.tools.codelife.configuration;

import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.util.ModelAndViewBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * 处理Rest接口请求时的异常
     * @param request
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(RestException.class)
    @ResponseBody
    public Object restError(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        Boolean isRestRequest = (Boolean) request.getAttribute("isRestRequest");
        isRestRequest = null == isRestRequest ? false : isRestRequest;
        RestException restException = (RestException) ex;
        
        Map<String, Object> map = new HashMap<>(4);
        map.put("exception", null != restException.getT() ? restException.getT() : restException);
        map.put("errorMessage", restException.getMessage());
        map.put("url", request.getRequestURL());
        map.put("statusCode",  restException.getCode());
        
        if (isRestRequest) {
            return map;
        }
        
        return ModelAndViewBuilder.of("error")
                .setDatasFromMap(map)
                .build();
    }
}
