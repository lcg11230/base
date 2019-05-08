package com.lcg.base.surpport;

import com.lcg.base.surpport.entity.EntityColumn;
import com.lcg.base.surpport.entity.EntityHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class SqlHelper {
    public SqlHelper() {
    }

    public static String getDynamicTableName(Class<?> entityClass, String tableName) {
        return IDynamicTableName.class.isAssignableFrom(entityClass) ? "<if test=\"@tk.mybatis.mapper.util.OGNL@isDynamicParameter(_parameter) and dynamicTableName != null and dynamicTableName != ''\">\n${dynamicTableName}\n</if>\n<if test=\"@tk.mybatis.mapper.util.OGNL@isNotDynamicParameter(_parameter) or dynamicTableName == null or dynamicTableName == ''\">\n" + tableName + "\n</if>" : tableName;
    }

    public static String getDynamicTableName(Class<?> entityClass, String tableName, String parameterName) {
        if (IDynamicTableName.class.isAssignableFrom(entityClass)) {
            return StringUtils.isNotEmpty(parameterName) ? "<if test=\"@tk.mybatis.mapper.util.OGNL@isDynamicParameter(" + parameterName + ") and " + parameterName + ".dynamicTableName != null and " + parameterName + ".dynamicTableName != ''\">\n${" + parameterName + ".dynamicTableName}\n</if>\n<if test=\"@tk.mybatis.mapper.util.OGNL@isNotDynamicParameter(" + parameterName + ") or " + parameterName + ".dynamicTableName == null or " + parameterName + ".dynamicTableName == ''\">\n" + tableName + "\n</if>" : getDynamicTableName(entityClass, tableName);
        } else {
            return tableName;
        }
    }

    public static String getBindCache(EntityColumn column) {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"");
        sql.append(column.getProperty()).append("_cache\" ");
        sql.append("value=\"").append(column.getProperty()).append("\"/>");
        return sql.toString();
    }

    public static String getBindValue(EntityColumn column, String value) {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"");
        sql.append(column.getProperty()).append("_bind\" ");
        sql.append("value='").append(value).append("'/>");
        return sql.toString();
    }

    public static String getIfCacheNotNull(EntityColumn column, String contents) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"").append(column.getProperty()).append("_cache != null\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }

    public static String getIfCacheIsNull(EntityColumn column, String contents) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"").append(column.getProperty()).append("_cache == null\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }

    public static String getIfNotNull(EntityColumn column, String contents, boolean empty) {
        return getIfNotNull((String)null, column, contents, empty);
    }

    public static String getIfIsNull(EntityColumn column, String contents, boolean empty) {
        return getIfIsNull((String)null, column, contents, empty);
    }

    public static String getIfNotNull(String entityName, EntityColumn column, String contents, boolean empty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"");
        if (StringUtils.isNotEmpty(entityName)) {
            sql.append(entityName).append(".");
        }

        sql.append(column.getProperty()).append(" != null");
        if (empty && column.getJavaType().equals(String.class)) {
            sql.append(" and ");
            if (StringUtils.isNotEmpty(entityName)) {
                sql.append(entityName).append(".");
            }

            sql.append(column.getProperty()).append(" != '' ");
        }

        sql.append("\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }

    public static String getIfIsNull(String entityName, EntityColumn column, String contents, boolean empty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"");
        if (StringUtils.isNotEmpty(entityName)) {
            sql.append(entityName).append(".");
        }

        sql.append(column.getProperty()).append(" == null");
        if (empty && column.getJavaType().equals(String.class)) {
            sql.append(" or ");
            if (StringUtils.isNotEmpty(entityName)) {
                sql.append(entityName).append(".");
            }

            sql.append(column.getProperty()).append(" == '' ");
        }

        sql.append("\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }

    public static String getAllColumns(Class<?> entityClass) {
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        StringBuilder sql = new StringBuilder();
        Iterator var3 = columnList.iterator();

        while(var3.hasNext()) {
            EntityColumn entityColumn = (EntityColumn)var3.next();
            sql.append(entityColumn.getColumn()).append(",");
        }

        return sql.substring(0, sql.length() - 1);
    }

    public static String selectAllColumns(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(getAllColumns(entityClass));
        sql.append(" ");
        return sql.toString();
    }

    public static String selectCount(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
        if (pkColumns.size() == 1) {
            sql.append("COUNT(").append(((EntityColumn)pkColumns.iterator().next()).getColumn()).append(") ");
        } else {
            sql.append("COUNT(*) ");
        }

        return sql.toString();
    }

    public static String selectCountExists(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CASE WHEN ");
        Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
        if (pkColumns.size() == 1) {
            sql.append("COUNT(").append(((EntityColumn)pkColumns.iterator().next()).getColumn()).append(") ");
        } else {
            sql.append("COUNT(*) ");
        }

        sql.append(" > 0 THEN 1 ELSE 0 END AS result ");
        return sql.toString();
    }

    public static String fromTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" FROM ");
        sql.append(getDynamicTableName(entityClass, defaultTableName));
        sql.append(" ");
        return sql.toString();
    }

    public static String updateTable(Class<?> entityClass, String defaultTableName) {
        return updateTable(entityClass, defaultTableName, (String)null);
    }

    public static String updateTable(Class<?> entityClass, String defaultTableName, String entityName) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(getDynamicTableName(entityClass, defaultTableName, entityName));
        sql.append(" ");
        return sql.toString();
    }

    public static String deleteFromTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(getDynamicTableName(entityClass, defaultTableName));
        sql.append(" ");
        return sql.toString();
    }

    public static String insertIntoTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(getDynamicTableName(entityClass, defaultTableName));
        sql.append(" ");
        return sql.toString();
    }

    public static String insertColumns(Class<?> entityClass, boolean skipId, boolean notNull, boolean notEmpty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Iterator var6 = columnList.iterator();

        while(true) {
            EntityColumn column;
            do {
                do {
                    if (!var6.hasNext()) {
                        sql.append("</trim>");
                        return sql.toString();
                    }

                    column = (EntityColumn)var6.next();
                } while(!column.isInsertable());
            } while(skipId && column.isId());

            if (notNull) {
                sql.append(getIfNotNull(column, column.getColumn() + ",", notEmpty));
            } else {
                sql.append(column.getColumn() + ",");
            }
        }
    }

    public static String insertValuesColumns(Class<?> entityClass, boolean skipId, boolean notNull, boolean notEmpty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\",\">");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Iterator var6 = columnList.iterator();

        while(true) {
            EntityColumn column;
            do {
                do {
                    if (!var6.hasNext()) {
                        sql.append("</trim>");
                        return sql.toString();
                    }

                    column = (EntityColumn)var6.next();
                } while(!column.isInsertable());
            } while(skipId && column.isId());

            if (notNull) {
                sql.append(getIfNotNull(column, column.getColumnHolder() + ",", notEmpty));
            } else {
                sql.append(column.getColumnHolder() + ",");
            }
        }
    }

    public static String updateSetColumns(Class<?> entityClass, String entityName, boolean notNull, boolean notEmpty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<set>");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Iterator var6 = columnList.iterator();

        while(var6.hasNext()) {
            EntityColumn column = (EntityColumn)var6.next();
            if (!column.isId() && column.isUpdatable()) {
                if (notNull) {
                    sql.append(getIfNotNull(entityName, column, column.getColumnEqualsHolder(entityName) + ",", notEmpty));
                } else {
                    sql.append(column.getColumnEqualsHolder(entityName) + ",");
                }
            }
        }

        sql.append("</set>");
        return sql.toString();
    }

    public static String wherePKColumns(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        Iterator var3 = columnList.iterator();

        while(var3.hasNext()) {
            EntityColumn column = (EntityColumn)var3.next();
            sql.append(" AND " + column.getColumnEqualsHolder());
        }

        sql.append("</where>");
        return sql.toString();
    }

    public static String whereAllIfColumns(Class<?> entityClass, boolean empty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Iterator var4 = columnList.iterator();

        while(var4.hasNext()) {
            EntityColumn column = (EntityColumn)var4.next();
            sql.append(getIfNotNull(column, " AND " + column.getColumnEqualsHolder(), empty));
        }

        sql.append("</where>");
        return sql.toString();
    }

    public static String orderByDefault(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        String orderByClause = EntityHelper.getOrderByClause(entityClass);
        if (orderByClause.length() > 0) {
            sql.append(" ORDER BY ");
            sql.append(orderByClause);
        }

        return sql.toString();
    }

    public static String exampleSelectColumns(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"@tk.mybatis.mapper.util.OGNL@hasSelectColumns(_parameter)\">");
        sql.append("<foreach collection=\"_parameter.selectColumns\" item=\"selectColumn\" separator=\",\">");
        sql.append("${selectColumn}");
        sql.append("</foreach>");
        sql.append("</if>");
        sql.append("<if test=\"@tk.mybatis.mapper.util.OGNL@hasNoSelectColumns(_parameter)\">");
        sql.append(getAllColumns(entityClass));
        sql.append("</if>");
        return sql.toString();
    }

    public static String exampleCountColumn(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<choose>");
        sql.append("<when test=\"@tk.mybatis.mapper.util.OGNL@hasCountColumn(_parameter)\">");
        sql.append("COUNT(${countColumn})");
        sql.append("</when>");
        sql.append("<otherwise>");
        sql.append("COUNT(0)");
        sql.append("</otherwise>");
        sql.append("</choose>");
        sql.append("<if test=\"@tk.mybatis.mapper.util.OGNL@hasNoSelectColumns(_parameter)\">");
        sql.append(getAllColumns(entityClass));
        sql.append("</if>");
        return sql.toString();
    }

    public static String exampleOrderBy(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"orderByClause != null\">");
        sql.append("order by ${orderByClause}");
        sql.append("</if>");
        String orderByClause = EntityHelper.getOrderByClause(entityClass);
        if (orderByClause.length() > 0) {
            sql.append("<if test=\"orderByClause == null\">");
            sql.append("ORDER BY " + orderByClause);
            sql.append("</if>");
        }

        return sql.toString();
    }

    public static String exampleForUpdate() {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"@tk.mybatis.mapper.util.OGNL@hasForUpdate(_parameter)\">");
        sql.append("FOR UPDATE");
        sql.append("</if>");
        return sql.toString();
    }

    public static String exampleCheck(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"checkExampleEntityClass\" value=\"@tk.mybatis.mapper.util.OGNL@checkExampleEntityClass(_parameter, '");
        sql.append(entityClass.getCanonicalName());
        sql.append("')\"/>");
        return sql.toString();
    }

    public static String exampleWhereClause() {
        return "<if test=\"_parameter != null\"><where>\n  <foreach collection=\"oredCriteria\" item=\"criteria\" separator=\"or\">\n    <if test=\"criteria.valid\">\n      <trim prefix=\"(\" prefixOverrides=\"and\" suffix=\")\">\n        <foreach collection=\"criteria.criteria\" item=\"criterion\">\n          <choose>\n            <when test=\"criterion.noValue\">\n              and ${criterion.condition}\n            </when>\n            <when test=\"criterion.singleValue\">\n              and ${criterion.condition} #{criterion.value}\n            </when>\n            <when test=\"criterion.betweenValue\">\n              and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n            </when>\n            <when test=\"criterion.listValue\">\n              and ${criterion.condition}\n              <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n                #{listItem}\n              </foreach>\n            </when>\n          </choose>\n        </foreach>\n      </trim>\n    </if>\n  </foreach>\n</where></if>";
    }

    public static String updateByExampleWhereClause() {
        return "<where>\n  <foreach collection=\"example.oredCriteria\" item=\"criteria\" separator=\"or\">\n    <if test=\"criteria.valid\">\n      <trim prefix=\"(\" prefixOverrides=\"and\" suffix=\")\">\n        <foreach collection=\"criteria.criteria\" item=\"criterion\">\n          <choose>\n            <when test=\"criterion.noValue\">\n              and ${criterion.condition}\n            </when>\n            <when test=\"criterion.singleValue\">\n              and ${criterion.condition} #{criterion.value}\n            </when>\n            <when test=\"criterion.betweenValue\">\n              and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n            </when>\n            <when test=\"criterion.listValue\">\n              and ${criterion.condition}\n              <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n                #{listItem}\n              </foreach>\n            </when>\n          </choose>\n        </foreach>\n      </trim>\n    </if>\n  </foreach>\n</where>";
    }
}
