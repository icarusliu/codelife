package com.liuqi.tools.codelife.util;

import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * ModelAndView类的建造器
 * 使用方式：
 * ModelAndView view = ModelAndViewBuilder.of("/test.html").setData("test.html", "testValue")
 *                  .setData("test1", "test1Value").build();
 *
 * @Author: LiuQI
 * @Created: 2018/3/26 19:46
 * @Version: V1.0
 **/
public class ModelAndViewBuilder {
    /**
     * 设置成私有防止调用
     */
    private ModelAndViewBuilder() {
    
    }
    
    private ModelAndView modelAndView;
    
    /**
     * 使用视图名称来构造ModelAndViewBuilder
     *
     * @param view 视图名称
     * @return 返回Builder对象
     */
    public static ModelAndViewBuilder of(String view) {
        ModelAndViewBuilder builder = new ModelAndViewBuilder();
        builder.modelAndView = new ModelAndView(view);
        return builder;
    }
    
    /**
     * 向ModelAndView中设置数据
     *
     * @param key 数据名称
     * @param value 数据
     * @return 返回Builder对象
     */
    public ModelAndViewBuilder setData(String key, Object value) {
        modelAndView.addObject(key, value);
        return this;
    }
    
    /**
     * 使用Map来设置数据到ModelAndView对象中
     *
     * @param map
     * @return
     */
    public ModelAndViewBuilder setDatasFromMap(Map<String, Object> map) {
        map.keySet().forEach(item -> modelAndView.addObject(item, map.get(item)));
        
        return this;
    }
    
    /**
     * 生成ModelAndView
     *
     * @return 返回生成的ModelAndView
     */
    public ModelAndView build() {
        return modelAndView;
    }
}
