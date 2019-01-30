package com.liuqi.tools.codelife.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LiuQI
 * @Created: 2018/4/19 17:35
 * @Version: V1.0
 **/
public class MapBuilder implements Builder<Map<String, Object>> {
    /**
     * 防止外部实例化
     */
    private MapBuilder() {
        this.map = new HashMap<>();
    }
    
    private Map<String, Object> map;
    
    /**
     * 获取构造器对象
     *
     * @return 返回构造器对象
     */
    public static MapBuilder of() {
        return new MapBuilder();
    }
    
    /**
     * @param key
     * @param obj
     * @return
     */
    public MapBuilder put(String key, Object obj) {
        map.put(key, obj);
        return this;
    }
    
    @Override
    public Map<String, Object> build() {
        return map;
    }
}
