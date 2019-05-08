package com.lcg.base.surpport.sqlprovider;

import com.lcg.base.surpport.MapperHelper;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class EmptyProvider extends BaseSqlProvider {
    public EmptyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    @Override
    public boolean supportMethod(String msId) {
        return false;
    }
}
