package com.liuqi.tools.codelife.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 后端接口默认添加/api前缀，以方便在前后端分离模式下nginx区分前后端请求
        configurer.setPathMatcher( new AntPathMatcher() {
            @Override
            public String combine(String pattern1, String pattern2) {
                return "/api" + super.combine(pattern1, pattern2);
            }
        });
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(restOrNotHandlerInterceptor);
        registry.addInterceptor(logInterceptor);
    }
    
    /**
     * Add handlers to serve static resources such as images, js, and, css
     * files from specific locations under web application root, the classpath,
     * and others.
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "/upload/")
                .setCachePeriod(3600*24);
    }
}
