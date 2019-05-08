package com.lcg.base.mapper;

import com.lcg.base.surpport.sqlprovider.SpecialProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

import java.util.List;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public interface InsertListMapper<T> {
    @Options(
            useGeneratedKeys = false,
            keyProperty = "id"
    )
    @InsertProvider(
            type = SpecialProvider.class,
            method = "dynamicSQL"
    )
    int insertList(List<T> var1);
}
