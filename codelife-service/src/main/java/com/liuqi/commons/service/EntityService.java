package com.liuqi.commons.service;

import com.liuqi.commons.web.rest.PageData;

import java.util.List;
import java.util.Optional;

/**
 * .
 *
 * @author LiuQI 2019/3/6 15:26
 * @version V1.0
 **/
public interface EntityService<D> {
    void save(List<D> list);

    void save(D d);

    List<D> findAll();

    PageData<D> pageQuery(int offset, int pageSize);

    Optional<D> findOne(Long id);

    void delete(Long id);
}
