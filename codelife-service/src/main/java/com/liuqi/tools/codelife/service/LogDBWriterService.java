package com.liuqi.tools.codelife.service;


/**
 * 日志往DB记录的服务接口
 *
 * @Author: LiuQI
 * @Created: 2018/3/31 17:09
 * @Version: V1.0
 **/
public interface LogDBWriterService {
    
    /**
     * 记录日志
     *
     * @param logMessage 需要记录的日志信息
     */
    public void log(String logMessage);
}
