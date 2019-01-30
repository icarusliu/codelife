package com.liuqi.tools.codelife.util;

/**
 * 构建器接口
 *
 * @author qi.liu
 * @param <T>
 */
public interface Builder<T> {
    /**
     * 返回构建的对象
     *
     * @return
     */
   T build();
}
