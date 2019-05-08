package com.lcg.base.mapper;

import com.lcg.base.mapper.select.*;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public interface BaseSelectMapper<T> extends SelectOneMapper<T>, SelectMapper<T>, SelectAllMapper<T>, SelectCountMapper<T>, SelectByPrimaryKeyMapper<T>, ExistsWithPrimaryKeyMapper<T> {
}
