package com.lcg.base.mapper;

import com.lcg.base.mapper.insert.InsertMapper;
import com.lcg.base.mapper.insert.InsertSelectiveMapper;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public interface BaseInsertMapper<T> extends InsertMapper<T>, InsertSelectiveMapper<T> {
}
