package com.liuqi.tools.codelife.service.query;

public class PageQuery<T> {
    private int pageNo;
    private int pageSize;
    private T query;

    public int getPageNo() {
        return this.pageNo;
    }

    public PageQuery<T> setPageNo(int pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public PageQuery<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public T getQuery() {
        return query;
    }

    public PageQuery<T> setQuery(T t) {
        this.query = t;
        return this;
    }
}