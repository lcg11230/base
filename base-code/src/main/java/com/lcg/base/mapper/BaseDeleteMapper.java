package com.lcg.base.mapper;

import com.lcg.base.mapper.delete.DeleteByPrimaryKeyMapper;
import com.lcg.base.mapper.delete.DeleteMapper;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public interface BaseDeleteMapper<T> extends DeleteMapper<T>, DeleteByPrimaryKeyMapper<T> {
}
