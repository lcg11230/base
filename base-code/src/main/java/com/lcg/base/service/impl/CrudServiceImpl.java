package com.lcg.base.service.impl;

import com.lcg.base.entity.BaseEntity;
import com.lcg.base.mapper.BaseMapper;
import com.lcg.base.service.CrudService;

import java.io.Serializable;

/**
 * Created by johnny on 2019/5/8.
 * 基础增删改查 实现类
 *
 * @author johnny
 */
public class CrudServiceImpl<TEntity extends BaseEntity> implements CrudService<TEntity> {

    private BaseMapper<TEntity> baseMapper;

    protected CrudServiceImpl(BaseMapper<TEntity> baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public TEntity find(Serializable id) {
        return null;
    }

    @Override
    public int add(TEntity entity) {
        return 0;
    }

    @Override
    public int update(TEntity entity) {
        return 0;
    }

    @Override
    public int delete(Serializable id) {
        return 0;
    }


}
