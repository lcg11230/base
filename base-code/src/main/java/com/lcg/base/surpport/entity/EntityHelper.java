package com.lcg.base.surpport.entity;

import com.lcg.base.surpport.*;
import com.lcg.base.surpport.annotation.ColumnType;
import com.lcg.base.surpport.annotation.NameStyle;
import com.lcg.base.utils.StringConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;

import javax.persistence.*;
import java.util.*;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class EntityHelper {
    private static final Map<Class<?>, EntityTable> entityTableMap = new HashMap();

    public EntityHelper() {
    }

    public static EntityTable getEntityTable(Class<?> entityClass) {
        EntityTable entityTable = (EntityTable) entityTableMap.get(entityClass);
        if (entityTable == null) {
            throw new MapperException("无法获取实体类" + entityClass.getCanonicalName() + "对应的表名!");
        } else {
            return entityTable;
        }
    }

    public static String getOrderByClause(Class<?> entityClass) {
        EntityTable table = getEntityTable(entityClass);
        if (table.getOrderByClause() != null) {
            return table.getOrderByClause();
        } else {
            StringBuilder orderBy = new StringBuilder();
            Iterator var3 = table.getEntityClassColumns().iterator();

            while (var3.hasNext()) {
                EntityColumn column = (EntityColumn) var3.next();
                if (column.getOrderBy() != null) {
                    if (orderBy.length() != 0) {
                        orderBy.append(",");
                    }

                    orderBy.append(column.getColumn()).append(" ").append(column.getOrderBy());
                }
            }

            table.setOrderByClause(orderBy.toString());
            return table.getOrderByClause();
        }
    }

    public static Set<EntityColumn> getColumns(Class<?> entityClass) {
        return getEntityTable(entityClass).getEntityClassColumns();
    }

    public static Set<EntityColumn> getPKColumns(Class<?> entityClass) {
        return getEntityTable(entityClass).getEntityClassPKColumns();
    }

    public static String getSelectColumns(Class<?> entityClass) {
        EntityTable entityTable = getEntityTable(entityClass);
        if (entityTable.getBaseSelect() != null) {
            return entityTable.getBaseSelect();
        } else {
            Set<EntityColumn> columnList = getColumns(entityClass);
            StringBuilder selectBuilder = new StringBuilder();
            boolean skipAlias = Map.class.isAssignableFrom(entityClass);
            Iterator var5 = columnList.iterator();

            while (true) {
                while (var5.hasNext()) {
                    EntityColumn entityColumn = (EntityColumn) var5.next();
                    selectBuilder.append(entityColumn.getColumn());
                    if (!skipAlias && !entityColumn.getColumn().equalsIgnoreCase(entityColumn.getProperty())) {
                        if (entityColumn.getColumn().substring(1, entityColumn.getColumn().length() - 1).equalsIgnoreCase(entityColumn.getProperty())) {
                            selectBuilder.append(",");
                        } else {
                            selectBuilder.append(" AS ").append(entityColumn.getProperty()).append(",");
                        }
                    } else {
                        selectBuilder.append(",");
                    }
                }

                entityTable.setBaseSelect(selectBuilder.substring(0, selectBuilder.length() - 1));
                return entityTable.getBaseSelect();
            }
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static String getAllColumns(Class<?> entityClass) {
        Set<EntityColumn> columnList = getColumns(entityClass);
        StringBuilder selectBuilder = new StringBuilder();
        Iterator var3 = columnList.iterator();

        while (var3.hasNext()) {
            EntityColumn entityColumn = (EntityColumn) var3.next();
            selectBuilder.append(entityColumn.getColumn()).append(",");
        }

        return selectBuilder.substring(0, selectBuilder.length() - 1);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static String getPrimaryKeyWhere(Class<?> entityClass) {
        Set<EntityColumn> entityColumns = getPKColumns(entityClass);
        StringBuilder whereBuilder = new StringBuilder();
        Iterator var3 = entityColumns.iterator();

        while (var3.hasNext()) {
            EntityColumn column = (EntityColumn) var3.next();
            whereBuilder.append(column.getColumnEqualsHolder()).append(" AND ");
        }

        return whereBuilder.substring(0, whereBuilder.length() - 4);
    }

    public static synchronized void initEntityNameMap(Class<?> entityClass, Config config) {
        if (entityTableMap.get(entityClass) == null) {
            Style style = config.getStyle();
            if (entityClass.isAnnotationPresent(NameStyle.class)) {
                NameStyle nameStyle = (NameStyle) entityClass.getAnnotation(NameStyle.class);
                style = nameStyle.value();
            }

            EntityTable entityTable = null;
            Table fields;
            if (entityClass.isAnnotationPresent(Table.class)) {
                fields = (Table) entityClass.getAnnotation(Table.class);
                if (!fields.name().equals("")) {
                    entityTable = new EntityTable(entityClass);
                    entityTable.setTable(fields);
                }
            }

            if (entityTable == null) {
                entityTable = new EntityTable(entityClass);
                entityTable.setName(StringConvertUtils.convertByStyle(entityClass.getSimpleName(), style));
            }

            entityTable.setEntityClassColumns(new LinkedHashSet());
            entityTable.setEntityClassPKColumns(new LinkedHashSet());

            List entityFields;
            if (config.isEnableMethodAnnotation()) {
                entityFields = FieldHelper.getAll(entityClass);
            } else {
                entityFields = FieldHelper.getFields(entityClass);
            }

            Iterator var5 = entityFields.iterator();

            while (true) {
                EntityField field;
                do {
                    if (!var5.hasNext()) {
                        if (entityTable.getEntityClassPKColumns().size() == 0) {
                            entityTable.setEntityClassPKColumns(entityTable.getEntityClassColumns());
                        }

                        entityTable.initPropertyMap();
                        entityTableMap.put(entityClass, entityTable);
                        return;
                    }

                    field = (EntityField) var5.next();
                } while (config.isUseSimpleType() && !SimpleTypeUtil.isSimpleType(field.getJavaType()));

                processField(entityTable, style, field);
            }
        }
    }

    private static void processField(EntityTable entityTable, Style style, EntityField field) {
        if (!field.isAnnotationPresent(Transient.class)) {
            EntityColumn entityColumn = new EntityColumn(entityTable);
            if (field.isAnnotationPresent(Id.class)) {
                entityColumn.setId(true);
            }

            String columnName = null;
            if (field.isAnnotationPresent(Column.class)) {
                Column column = (Column) field.getAnnotation(Column.class);
                columnName = column.name();
                entityColumn.setUpdatable(column.updatable());
                entityColumn.setInsertable(column.insertable());
            }

            if (field.isAnnotationPresent(ColumnType.class)) {
                ColumnType columnType = (ColumnType) field.getAnnotation(ColumnType.class);
                if (StringUtils.isEmpty(columnName) && StringUtils.isNotEmpty(columnType.column())) {
                    columnName = columnType.column();
                }

                if (columnType.jdbcType() != JdbcType.UNDEFINED) {
                    entityColumn.setJdbcType(columnType.jdbcType());
                }

                if (columnType.typeHandler() != UnknownTypeHandler.class) {
                    entityColumn.setTypeHandler(columnType.typeHandler());
                }
            }

            if (StringUtils.isEmpty(columnName)) {
                columnName = StringConvertUtils.convertByStyle(field.getName(), style);
            }

            entityColumn.setProperty(field.getName());
            entityColumn.setColumn(columnName);
            entityColumn.setJavaType(field.getJavaType());
            if (field.isAnnotationPresent(OrderBy.class)) {
                OrderBy orderBy = (OrderBy) field.getAnnotation(OrderBy.class);
                if (orderBy.value().equals("")) {
                    entityColumn.setOrderBy("ASC");
                } else {
                    entityColumn.setOrderBy(orderBy.value());
                }
            }

            if (field.isAnnotationPresent(SequenceGenerator.class)) {
                SequenceGenerator sequenceGenerator = (SequenceGenerator) field.getAnnotation(SequenceGenerator.class);
                if (sequenceGenerator.sequenceName().equals("")) {
                    throw new MapperException(entityTable.getEntityClass() + "字段" + field.getName() + "的注解@SequenceGenerator未指定sequenceName!");
                }

                entityColumn.setSequenceName(sequenceGenerator.sequenceName());
            } else if (field.isAnnotationPresent(GeneratedValue.class)) {
                GeneratedValue generatedValue = (GeneratedValue) field.getAnnotation(GeneratedValue.class);
                if (generatedValue.generator().equals("UUID")) {
                    entityColumn.setUuid(true);
                } else if (generatedValue.generator().equals("JDBC")) {
                    entityColumn.setIdentity(true);
                    entityColumn.setGenerator("JDBC");
                    entityTable.setKeyProperties(entityColumn.getProperty());
                    entityTable.setKeyColumns(entityColumn.getColumn());
                } else {
                    if (generatedValue.strategy() != GenerationType.IDENTITY) {
                        throw new MapperException(field.getName() + " - 该字段@GeneratedValue配置只允许以下几种形式:\n1.全部数据库通用的@GeneratedValue(generator=\"UUID\")\n2.useGeneratedKeys的@GeneratedValue(generator=\\\"JDBC\\\")  \n3.类似mysql数据库的@GeneratedValue(strategy=GenerationType.IDENTITY[,generator=\"Mysql\"])");
                    }

                    entityColumn.setIdentity(true);
                    if (!generatedValue.generator().equals("")) {
                        String generator = null;
                        IdentityDialect identityDialect = IdentityDialect.getDatabaseDialect(generatedValue.generator());
                        if (identityDialect != null) {
                            generator = identityDialect.getIdentityRetrievalStatement();
                        } else {
                            generator = generatedValue.generator();
                        }

                        entityColumn.setGenerator(generator);
                    }
                }
            }

            entityTable.getEntityClassColumns().add(entityColumn);
            if (entityColumn.isId()) {
                entityTable.getEntityClassPKColumns().add(entityColumn);
            }

        }
    }
}
