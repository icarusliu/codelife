package com.liuqi.tools.codelife.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author: LiuQI
 * @Created: 2018/4/19 10:25
 * @Version: V1.0
 **/
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Resource
    private RestOrNotHandlerInterceptor restOrNotHandlerInterceptor;

    @Resource
    private RequestLogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(restOrNotHandlerInterceptor);
        registry.addInterceptor(logInterceptor);
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("http://localhost:8080")
                .allowCredentials(true);

    }
}
