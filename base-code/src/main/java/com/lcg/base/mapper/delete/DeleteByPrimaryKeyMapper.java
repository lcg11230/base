package com.lcg.base.mapper.delete;

import com.lcg.base.surpport.sqlprovider.BaseDeleteProvider;
import org.apache.ibatis.annotations.DeleteProvider;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public interface DeleteByPrimaryKeyMapper<T> {
    @DeleteProvider(
            type = BaseDeleteProvider.class,
            method = "dynamicSQL"
    )
    int deleteByPrimaryKey(Object var1);
}
