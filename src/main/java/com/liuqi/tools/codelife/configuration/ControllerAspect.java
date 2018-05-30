package com.liuqi.tools.codelife.configuration;

import com.liuqi.tools.codelife.service.LogDBWriterService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller的Aspect，完成以下功能：
 * <ul>
 *     <li>每个请求的相关日志打印</li>
 *     <li>登录网站的请求日志记录</li>
 * </ul>
 *
 * @Author: LiuQI
 * @Created: 2018/3/31 12:41
 * @Version: V1.0
 **/
@Aspect
@Component
public class ControllerAspect {
    @Autowired
    private LogDBWriterService logDBWriterService;
    
    /**
     * Controller方法执行前后切面处理
     * 增加日志打印和日志存库操作
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(public * com.liuqi.tools.codelife.controllers..*.*(..))")
    public Object beforeRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        String spliter = "\n----------------------------------------------------------------------------------------\n";
        
        long startTime = System.currentTimeMillis();
        
        //组装参数信息
        Object[] params = joinPoint.getArgs();
        String argsInfo = "";
        for (int i = 0, length = params.length; i < length; i++) {
            if (null == params[i]) {
                continue;
            }
            
            if (0 == i) {
                argsInfo += params[i].toString();
            } else {
                argsInfo += "," + params[i].toString();
            }
        }
        
        String infos = "\ncontroller: " + joinPoint.toString()
                + "\nargs: " + argsInfo;
        
        //打印前置日志
        logger.info(spliter + "Begin to process a request: " + infos + spliter);
        
        //提交记录日志的请求到线程池中，由线程池来完成日志的存库处理
        logDBWriterService.log(joinPoint.toLongString());
        
        Object result = "";

        try {
            //调用实际处理方法
            result = joinPoint.proceed();
            return result;
        } finally {
            //打印后置日志
            logger.info(spliter  + "End of processing request:" + infos
                    + "\nresult: " + result
                    + "\nuse time: " + (System.currentTimeMillis() - startTime) + "ms"
                    + spliter);
        }

    }
    
    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);
    private static final String STATUS_CODE_SUCCEEDED = "200";
    private static final String STATUS_CODE_INTERNAL_ERROR = "500";
}
