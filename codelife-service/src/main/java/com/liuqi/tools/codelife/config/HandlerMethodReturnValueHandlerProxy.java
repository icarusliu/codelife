package com.liuqi.tools.codelife.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * REST类型的返回值处理器
 * 将REST的返回结果进行进一步的封装，如原本返回的是data，那么封装后将会是：
 * {statusCode: '', errorMessage: '', exception: {}, data: data}
 *
 * @author LiuQI 2018/5/30 10:48
 * @version V1.0
 **/
public class HandlerMethodReturnValueHandlerProxy implements HandlerMethodReturnValueHandler {
    private HandlerMethodReturnValueHandler proxyObject;

    public HandlerMethodReturnValueHandlerProxy(HandlerMethodReturnValueHandler proxyObject) {
        this.proxyObject = proxyObject;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return proxyObject.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        Map<String, Object> resultMap = new HashMap<>(3);

        resultMap.put("statusCode", STATUS_CODE_SUCCEEDED);
        resultMap.put("errorMessage", "");
        resultMap.put("data", returnValue);

        proxyObject.handleReturnValue(resultMap, returnType, mavContainer, webRequest);
    }

    private static final String STATUS_CODE_SUCCEEDED = "200";
}
