package com.lcg.base.service;

import com.lcg.base.entity.BaseEntity;

import java.io.Serializable;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public interface CrudService<TEntity extends BaseEntity> {

    /**
     * 查询
     *
     * @param id 唯一主键
     * @return 实体对象
     */
    TEntity find(Serializable id);

    /**
     * 添加
     *
     * @param entity 对象
     * @return
     */
    int add(TEntity entity);

    /**
     * 更新
     *
     * @param entity 对象
     * @return
     */
    int update(TEntity entity);

    /**
     * 删除
     *
     * @param id 唯一主键
     * @return
     */
    int delete(Serializable id);


}
