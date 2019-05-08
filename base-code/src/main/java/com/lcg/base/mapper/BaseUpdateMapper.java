package com.lcg.base.mapper;

import com.lcg.base.mapper.update.UpdateByPrimaryKeyMapper;
import com.lcg.base.mapper.update.UpdateByPrimaryKeySelectiveMapper;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public interface BaseUpdateMapper <T> extends UpdateByPrimaryKeyMapper<T>, UpdateByPrimaryKeySelectiveMapper<T> {
}
