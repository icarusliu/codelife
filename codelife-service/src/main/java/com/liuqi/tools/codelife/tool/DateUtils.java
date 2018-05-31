package com.liuqi.tools.codelife.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理类
 * 通过ThreadLocal保证线程安全
 *
 * @Author: LiuQI
 * @Created: 2018/3/27 8:44
 * @Version: V1.0
 **/
public final class DateUtils {
    private static ThreadLocal<SimpleDateFormat> timeFormatThreadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    
    /**
     * 日期串格式化，格式：yyyy-MM-dd HH:mm:ss
     * 线程安全
     *
     * @param date 需要格式化的日期串
     * @return 返回格式化后的字符串
     */
    public static String format(Date date) {
        return timeFormatThreadLocal.get().format(date);
    }
    
    /**
     * 由字符串转换成日期
     * 线程安全
     *
     * 当字符串不符合yyyy-MM-dd HH:mm:ss的格式时，抛出异常
     * @param dateFormat 需要转换成日期的字符中
     * @return 转换后的日期
     * @throws ParseException 如果字符串不满足yyyy-MM-dd HH:mm:ss时抛出异常
     */
    public static Date parse(String dateFormat) throws ParseException {
        return timeFormatThreadLocal.get().parse(dateFormat);
    }
    
    /**
     * 获取当前时间的字符串
     *
     * @return 返回格式化后的当前时间的字符串，格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDateStr() {
        return format(Calendar.getInstance().getTime());
    }
    
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
}
