package com.liuqi.tools.codelife.db.dao.typehandler;

import com.liuqi.tools.codelife.db.entity.CommentType;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;

/**
 * @Author: LiuQI
 * @Created: 2018/4/13 17:30
 * @Version: V1.0
 **/
public class CommentTypeHandler  extends EnumOrdinalTypeHandler {
    public CommentTypeHandler() {
        super(CommentType.class);
    }
}
