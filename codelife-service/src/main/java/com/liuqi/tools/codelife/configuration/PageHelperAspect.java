package com.liuqi.tools.codelife.configuration;

import com.github.pagehelper.PageHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 分页控件切面处理类
 * 查找Service中参数中包含有nowPage以及pageSize参数的方法，在执行命中方法前调用分页方法
 *
 * @Author: LiuQI
 * @Created: 2018/4/17 18:54
 * @Version: V1.0
 **/
@Aspect
@Component
public class PageHelperAspect {
    @Around(value = "execution(public * com.liuqi.tools.codelife.service..*.*(..))")
    public Object pageAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] params = joinPoint.getArgs();
        int nowPage = 1;
        int pageSize = 20;
        boolean findPageSize = false;
        
        for (int i = 0, length = paramNames.length; i < length; i++) {
            String paramName = paramNames[i];
            if ("nowPage".equals(paramName)) {
                nowPage = Integer.valueOf(params[i].toString());
            } else if ("pageSize".equals(paramName)) {
                findPageSize = true;
                pageSize = Integer.valueOf(params[i].toString());
            }
        }
        
        if (findPageSize) {
            PageHelper.startPage(nowPage, pageSize);
        }


        return joinPoint.proceed();
    }
    
    private static final Logger logger = LoggerFactory.getLogger(PageHelperAspect.class);
}
