package com.liuqi.commons.service;

import com.liuqi.commons.db.BaseRepository;
import com.liuqi.commons.web.rest.PageData;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * 抽象实体服务类
 *
 * @author LiuQI 2019/3/6 15:38
 * @version V1.0
 **/
public abstract class AbstractEntityService<E, D, R extends BaseRepository<E>, M extends EntityMapper<E, D>>
        implements EntityService<D> {
    protected R repository;
    protected M mapper;

    public AbstractEntityService(R repository, M mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(List<D> list) {
        repository.saveAll(mapper.toEntity(list));
    }

    @Override
    public D save(D d) {
        return mapper.toDto(repository.save(mapper.toEntity(d)));
    }

    @Override
    public List<D> findAll() {
        return mapper.toDto(repository.findAll());
    }

    @Override
    public List<D> findList(D example) {
        return mapper.toDto(repository.findAll(Example.of(mapper.toEntity(example))));
    }

    @Override
    public Optional<D> findOne(Integer id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public PageData<D> pageQuery(int offset, int pageSize) {
        int page = offset / pageSize;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<E> data = repository.findAll(pageRequest);
        List<D> list = data.map(mapper::toDto).getContent();
        PageData<D> pageData = new PageData<>();
        return pageData
                .setTotal(data.getTotalElements())
                .setRows(list);
    }
}
