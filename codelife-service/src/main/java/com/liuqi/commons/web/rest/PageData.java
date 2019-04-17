package com.liuqi.commons.web.rest;

import java.util.List;

/**
 * 分页数据对象
 *
 * @author LiuQI 2019/3/6 16:39
 * @version V1.0
 **/
public class PageData<D> {
    private List<D> rows;
    private long total;

    public List<D> getRows() {
        return rows;
    }

    public PageData<D> setRows(List<D> rows) {
        this.rows = rows;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public PageData<D> setTotal(long total) {
        this.total = total;
        return this;
    }
}
