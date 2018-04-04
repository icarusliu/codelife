package com.liuqi.tools.codelife.entity;

import org.apache.ibatis.type.EnumOrdinalTypeHandler;

/**
 * 文章状态Enum类转换器
 * 数据库中存储的是数字类型，转换器将数字类型转换成Enum；同时在插入数据库的时候也可以将其转换成数字
 * 注意需要在MyBatis的配置文件中进行注册方可使用
 *
 * @Author: LiuQI
 * @Created: 2018/3/26 16:48
 * @Version: V1.0
 **/
public class ArticleStatusTypeHandler extends EnumOrdinalTypeHandler {
    public ArticleStatusTypeHandler() {
        super(ArticleStatus.class);
    }
    
    
}
