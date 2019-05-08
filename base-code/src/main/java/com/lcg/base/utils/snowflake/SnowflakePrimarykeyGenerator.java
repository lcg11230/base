package com.lcg.base.utils.snowflake;

import com.lcg.base.utils.PrimarykeyGenerator;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class SnowflakePrimarykeyGenerator implements PrimarykeyGenerator {
    public SnowflakePrimarykeyGenerator() {
    }

    @Override
    public Long generateId(String tableName) {
        return IdWorker.getId();
    }

    @Override
    public Long[] generateIds(String tableName, int size) {
        Long[] ids = new Long[size];

        for (int i = 0; i < size; ++i) {
            ids[i] = this.generateId(tableName);
        }

        return ids;
    }
}
