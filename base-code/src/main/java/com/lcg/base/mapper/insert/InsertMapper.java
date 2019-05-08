package com.lcg.base.mapper.insert;

import com.lcg.base.surpport.sqlprovider.BaseInsertProvider;
import org.apache.ibatis.annotations.InsertProvider;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public interface InsertMapper<T> {

    @InsertProvider(
            type = BaseInsertProvider.class,
            method = "dynamicSQL"
    )
    int insert(T entity);

}
