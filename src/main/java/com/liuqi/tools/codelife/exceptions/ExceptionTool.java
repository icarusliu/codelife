package com.liuqi.tools.codelife.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 异常工具类
 *
 * @author LiuQI 2018/5/29 19:32
 * @version V1.0
 **/
public class ExceptionTool {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionTool.class);
    private static Map<String, ErrorInfo> errorInfoMap = new HashMap<>();

    private ExceptionTool() {

    }

    public static void init() throws IOException {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:./errorCode.properties");
        Properties properties = new Properties();
        properties.load(resource.getInputStream());

        properties.forEach((o1, o2) -> {
            String errorName = o1.toString();
            String errorInfoStr = o2.toString();
            String[] strs = errorInfoStr.split(",");
            ErrorInfo errorInfo = new ErrorInfo();

            System.out.println(errorInfoStr);

            errorInfo.code = strs[0];
            errorInfo.message = strs[1];

            errorInfoMap.put(errorName, errorInfo);
        });
    }

    /**
     * 根据异常名称获取异常信息
     *
     * @return
     */
    public static RestException getException(ErrorCodes errorCodes, Object...parameters) {
        ErrorInfo errorInfo = errorInfoMap.get(errorCodes.getErrorName());
        String message = errorInfo.message;
        if (null != parameters && 0 != parameters.length) {
            for (Object obj: parameters) {
                if (!message.contains("{}")) {
                    break;
                }

                message = message.replaceFirst("\\{\\}", obj.toString());
            }
        }

        return new RestException(errorInfo.code, message);
    }

    public static RestException getException(Throwable t, ErrorCodes errorCodes, Object...parameters) {
        return getException(errorCodes, parameters).setT(t);
    }

    private static class ErrorInfo {
        private String code;
        private String message;
    }

    public static void main(String[] args) throws IOException {

    }
}
