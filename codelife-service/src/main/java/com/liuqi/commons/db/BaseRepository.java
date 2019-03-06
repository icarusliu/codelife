package com.liuqi.commons.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 基础Repository
 * 继承于JpaRepository及JpaSpecificationExecutor两个接口
 *
 * @author LiuQI 2019/1/31 13:56
 * @version V1.0
 **/
@NoRepositoryBean
public interface BaseRepository<E> extends JpaRepository<E, Long>, JpaSpecificationExecutor<E> {
}

