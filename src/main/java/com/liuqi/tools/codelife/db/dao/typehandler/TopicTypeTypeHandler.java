package com.liuqi.tools.codelife.db.dao.typehandler;

import com.liuqi.tools.codelife.entity.TopicType;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;

/**
 * 专题类型转换器
 *
 * @Author: LiuQI
 * @Created: 2018/4/9 18:28
 * @Version: V1.0
 **/
public class TopicTypeTypeHandler extends EnumOrdinalTypeHandler {
    public TopicTypeTypeHandler() {
        super(TopicType.class);
    }
}
