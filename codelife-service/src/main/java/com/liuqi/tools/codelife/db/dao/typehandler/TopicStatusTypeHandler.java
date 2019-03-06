package com.liuqi.tools.codelife.db.dao.typehandler;

import com.liuqi.tools.codelife.db.entity.TopicStatus;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;

/**
 * 专题状态转换器
 *
 * @Author: LiuQI
 * @Created: 2018/4/9 18:26
 * @Version: V1.0
 **/
public class TopicStatusTypeHandler extends EnumOrdinalTypeHandler {
    public TopicStatusTypeHandler() {
        super(TopicStatus.class);
    }
}
