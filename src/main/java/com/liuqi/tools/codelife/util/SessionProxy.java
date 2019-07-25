package com.liuqi.tools.codelife.util;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Session操作代理类
 *
 * @Author: LiuQI
 * @Created: 2018/3/30 15:09
 * @Version: V1.0
 **/
public final class SessionProxy {
    private HttpSession session ;
    
    private SessionProxy(HttpSession session) {
        this.session = session;
    }
    
    /**
     * 使用传入的Session构造代理类
     *
     * @param session
     * @return
     */
    public static SessionProxy proxy(HttpSession session) {
        return new SessionProxy(session);
    }
    
    public SessionProxy setAttribute(String name, Object value) {
        this.session.setAttribute(name, value);
        return this;
    }
    
    /**
     * 设置Session中存储的Map中的值
     * @param mapName Sessioin中存储的Map名称
     * @param name 需要设置到Map中的数据的Key
     * @param value 需要设置到Map中的数据的Value
     * @param <T> 需要设置到Map中的数据Value的类型
     * @return
     */
    public <T> SessionProxy setMapAttribute(String mapName, String name, T value) {
        Map<String, T> map = (Map<String, T>) Optional.ofNullable(session.getAttribute(mapName))
                .orElseGet(() -> {
                    Map<String, T> result = new HashMap<>();
                    session.setAttribute(mapName, result);
                    return result;
                });
        
        map.put(name, value);
        
        return this;
    }
    
    /**
     * 从Session中获取存储在Map中的值
     * @param mapName Session中Map的名称
     * @param name 存储在Map中的Key
     * @param <T> 存储在Map中Value的类型
     * @return 存储在Map中的Value，如果没有则返回Null
     */
    public <T> T getMapAttribute(String mapName, String name) {
        Map<String, T> map = (Map<String, T>) Optional.ofNullable(session.getAttribute(mapName))
                .orElseGet(() -> {
                    Map<String, T> result = new HashMap<>();
                    session.setAttribute(mapName, result);
                    return result;
                });
        return map.get(name);
    }
    
    /**
     * 如果Session中指定的Map中有指定Key的数据，则返回False；否则将Value存储到对应的Key上，然后返回True
     *
     * @param mapName
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean notExistSetMapAttribute(String mapName, String key, T value) {
        T t = getMapAttribute(mapName, key);
        if (null != t) {
            return false;
        }
        
        setMapAttribute(mapName, key, value);
        return true;
    }
}
