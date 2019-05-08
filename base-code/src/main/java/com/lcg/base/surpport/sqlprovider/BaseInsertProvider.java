package com.lcg.base.surpport.sqlprovider;

import com.lcg.base.surpport.MapperException;
import com.lcg.base.surpport.MapperHelper;
import com.lcg.base.surpport.SqlHelper;
import com.lcg.base.surpport.entity.EntityColumn;
import com.lcg.base.surpport.entity.EntityHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class BaseInsertProvider extends BaseSqlProvider  {
    public BaseInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String insert(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Boolean hasIdentityKey = false;
        Iterator var6 = columnList.iterator();

        EntityColumn column;
        label71:
        do {
            while(true) {
                while(true) {
                    do {
                        do {
                            if (!var6.hasNext()) {
                                sql.append(SqlHelper.insertIntoTable(entityClass, this.tableName(entityClass)));
                                sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
                                sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
                                var6 = columnList.iterator();

                                while(var6.hasNext()) {
                                    column = (EntityColumn)var6.next();
                                    if (column.isInsertable()) {
                                        if (column.isIdentity()) {
                                            sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder((String)null, "_cache", ",")));
                                        } else {
                                            sql.append(SqlHelper.getIfNotNull(column, column.getColumnHolder((String)null, (String)null, ","), this.isNotEmpty()));
                                        }

                                        if (StringUtils.isNotEmpty(column.getSequenceName())) {
                                            sql.append(SqlHelper.getIfIsNull(column, this.getSeqNextVal(column) + " ,", false));
                                        } else if (column.isIdentity()) {
                                            sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder() + ","));
                                        } else if (column.isUuid()) {
                                            sql.append(SqlHelper.getIfIsNull(column, column.getColumnHolder((String)null, "_bind", ","), this.isNotEmpty()));
                                        } else {
                                            sql.append(SqlHelper.getIfIsNull(column, column.getColumnHolder((String)null, (String)null, ","), this.isNotEmpty()));
                                        }
                                    }
                                }

                                sql.append("</trim>");
                                return sql.toString();
                            }

                            column = (EntityColumn)var6.next();
                        } while(!column.isInsertable());
                    } while(StringUtils.isNotEmpty(column.getSequenceName()));

                    if (column.isIdentity()) {
                        sql.append(SqlHelper.getBindCache(column));
                        if (hasIdentityKey) {
                            continue label71;
                        }

                        this.newSelectKeyMappedStatement(ms, column);
                        hasIdentityKey = true;
                    } else if (column.isUuid()) {
                        sql.append(SqlHelper.getBindValue(column, this.getUUID()));
                    }
                }
            }
        } while(column.getGenerator() != null && column.getGenerator().equals("JDBC"));

        throw new MapperException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
    }

    public String insertSelective(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Boolean hasIdentityKey = false;
        Iterator var6 = columnList.iterator();

        EntityColumn column;
        label95:
        do {
            while(true) {
                while(true) {
                    do {
                        do {
                            if (!var6.hasNext()) {
                                sql.append(SqlHelper.insertIntoTable(entityClass, this.tableName(entityClass)));
                                sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
                                var6 = columnList.iterator();

                                while(true) {
                                    while(true) {
                                        do {
                                            if (!var6.hasNext()) {
                                                sql.append("</trim>");
                                                sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
                                                var6 = columnList.iterator();

                                                while(var6.hasNext()) {
                                                    column = (EntityColumn)var6.next();
                                                    if (column.isInsertable()) {
                                                        if (column.isIdentity()) {
                                                            sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder((String)null, "_cache", ",")));
                                                        } else {
                                                            sql.append(SqlHelper.getIfNotNull(column, column.getColumnHolder((String)null, (String)null, ","), this.isNotEmpty()));
                                                        }

                                                        if (StringUtils.isNotEmpty(column.getSequenceName())) {
                                                            sql.append(SqlHelper.getIfIsNull(column, this.getSeqNextVal(column) + " ,", this.isNotEmpty()));
                                                        } else if (column.isIdentity()) {
                                                            sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder() + ","));
                                                        } else if (column.isUuid()) {
                                                            sql.append(SqlHelper.getIfIsNull(column, column.getColumnHolder((String)null, "_bind", ","), this.isNotEmpty()));
                                                        }
                                                    }
                                                }

                                                sql.append("</trim>");
                                                return sql.toString();
                                            }

                                            column = (EntityColumn)var6.next();
                                        } while(!column.isInsertable());

                                        if (!StringUtils.isNotEmpty(column.getSequenceName()) && !column.isIdentity() && !column.isUuid()) {
                                            sql.append(SqlHelper.getIfNotNull(column, column.getColumn() + ",", this.isNotEmpty()));
                                        } else {
                                            sql.append(column.getColumn() + ",");
                                        }
                                    }
                                }
                            }

                            column = (EntityColumn)var6.next();
                        } while(!column.isInsertable());
                    } while(StringUtils.isNotEmpty(column.getSequenceName()));

                    if (column.isIdentity()) {
                        sql.append(SqlHelper.getBindCache(column));
                        if (hasIdentityKey) {
                            continue label95;
                        }

                        this.newSelectKeyMappedStatement(ms, column);
                        hasIdentityKey = true;
                    } else if (column.isUuid()) {
                        sql.append(SqlHelper.getBindValue(column, this.getUUID()));
                    }
                }
            }
        } while(column.getGenerator() != null && column.getGenerator().equals("JDBC"));

        throw new MapperException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
    }
}
