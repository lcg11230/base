package com.lcg.base.mapper.update;

import com.lcg.base.surpport.sqlprovider.BaseUpdateProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public interface UpdateByPrimaryKeySelectiveMapper<T> {
    @UpdateProvider(
            type = BaseUpdateProvider.class,
            method = "dynamicSQL"
    )
    @Options(
            useCache = false,
            useGeneratedKeys = false
    )
    int updateByPrimaryKeySelective(T var1);
}
