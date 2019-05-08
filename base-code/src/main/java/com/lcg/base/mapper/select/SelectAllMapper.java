package com.lcg.base.mapper.select;

import com.lcg.base.surpport.sqlprovider.BaseSelectProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public interface SelectAllMapper<T> {
    @SelectProvider(
            type = BaseSelectProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectAll();
}
