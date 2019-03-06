package com.liuqi.tools.codelife.config;

import com.liuqi.tools.codelife.service.VisitLogService;
import com.liuqi.tools.codelife.service.dto.VisitLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 请求拦截器
 * 记录请求数
 *
 * @author LiuQI 2019/3/6 10:27
 * @version V1.0
 **/
@Component
public class RequestLogInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLogInterceptor.class);

    @Resource
    private VisitLogService visitLogService;

    /**
     * This implementation always returns {@code true}.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        String requestUrl = request.getRequestURI();
        String remoteUser = request.getRemoteUser();
        String remoteHost = request.getRemoteHost();

        // admin的访问及liuqi的访问不需要记录日志
        if (Arrays.asList("admin", "liuqi", "test").contains(remoteUser)) {
            return true;
        }

        VisitLogDTO visitLogDTO = new VisitLogDTO()
                .setVisitTime(LocalDateTime.now())
                .setRequestUrl(requestUrl)
                .setUser(remoteUser)
                .setUserIp(remoteHost)
                .setParams(request.getParameterMap().toString());
        visitLogService.save(visitLogDTO);

        return true;
    }
}
