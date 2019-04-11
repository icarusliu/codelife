package com.liuqi.commons.web.rest;

import com.liuqi.commons.service.EntityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基础实体类控制器
 *
 * @author LiuQI 2019/3/6 15:59
 * @version V1.0
 **/
public abstract class BaseEntityController<D, S extends EntityService> {
    protected S entityService;

    public BaseEntityController(S entityService) {
        this.entityService = entityService;
    }

    @PostMapping
    public void save(@RequestBody D d) {
        entityService.save(d);
    }

    @GetMapping
    public List<D> findAll() {
        return entityService.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam("id") Integer id) {
        entityService.delete(id);
    }

    @GetMapping("/pageQuery")
    public PageData<D> pageQuery(@RequestParam(value = "offset") Integer offset,
                                           @RequestParam(value = "limit") Integer pageSize) {
        return entityService.pageQuery(offset, pageSize);
    }
}
