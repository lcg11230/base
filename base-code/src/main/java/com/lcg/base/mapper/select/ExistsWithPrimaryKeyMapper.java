package com.lcg.base.mapper.select;

import com.lcg.base.surpport.sqlprovider.BaseSelectProvider;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public interface ExistsWithPrimaryKeyMapper<T> {
    @SelectProvider(
            type = BaseSelectProvider.class,
            method = "dynamicSQL"
    )
    boolean existsWithPrimaryKey(Object var1);
}
