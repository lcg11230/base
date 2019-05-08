package com.lcg.base.surpport.sqlprovider;

import com.lcg.base.surpport.MapperHelper;
import com.lcg.base.surpport.SqlHelper;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class BaseUpdateProvider extends BaseSqlProvider {
    public BaseUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String updateByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.updateSetColumns(entityClass, (String) null, false, false));
        sql.append(SqlHelper.wherePKColumns(entityClass));
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.updateSetColumns(entityClass, (String) null, true, this.isNotEmpty()));
        sql.append(SqlHelper.wherePKColumns(entityClass));
        return sql.toString();
    }
}
