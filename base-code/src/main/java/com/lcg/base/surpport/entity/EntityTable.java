package com.lcg.base.surpport.entity;

import com.lcg.base.surpport.MapperException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

import javax.persistence.Table;
import java.util.*;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class EntityTable {
    private String name;
    private String catalog;
    private String schema;
    private String orderByClause;
    private String baseSelect;
    private Set<EntityColumn> entityClassColumns;
    private Set<EntityColumn> entityClassPKColumns;
    private List<String> keyProperties;
    private List<String> keyColumns;
    private ResultMap resultMap;
    protected Map<String, EntityColumn> propertyMap;
    private Class<?> entityClass;

    public EntityTable(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    public void setTable(Table table) {
        this.name = table.name();
        this.catalog = table.catalog();
        this.schema = table.schema();
    }

    public void setKeyColumns(List<String> keyColumns) {
        this.keyColumns = keyColumns;
    }

    public void setKeyProperties(List<String> keyProperties) {
        this.keyProperties = keyProperties;
    }

    public String getOrderByClause() {
        return this.orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog() {
        return this.catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getBaseSelect() {
        return this.baseSelect;
    }

    public void setBaseSelect(String baseSelect) {
        this.baseSelect = baseSelect;
    }

    public String getPrefix() {
        if (StringUtils.isNotEmpty(this.catalog)) {
            return this.catalog;
        } else {
            return StringUtils.isNotEmpty(this.schema) ? this.schema : "";
        }
    }

    public Set<EntityColumn> getEntityClassColumns() {
        return this.entityClassColumns;
    }

    public void setEntityClassColumns(Set<EntityColumn> entityClassColumns) {
        this.entityClassColumns = entityClassColumns;
    }

    public Set<EntityColumn> getEntityClassPKColumns() {
        return this.entityClassPKColumns;
    }

    public void setEntityClassPKColumns(Set<EntityColumn> entityClassPKColumns) {
        this.entityClassPKColumns = entityClassPKColumns;
    }

    public String[] getKeyProperties() {
        return this.keyProperties != null && this.keyProperties.size() > 0 ? (String[])this.keyProperties.toArray(new String[0]) : new String[0];
    }

    public void setKeyProperties(String keyProperty) {
        if (this.keyProperties == null) {
            this.keyProperties = new ArrayList();
            this.keyProperties.add(keyProperty);
        } else {
            this.keyProperties.add(keyProperty);
        }

    }

    public String[] getKeyColumns() {
        return this.keyColumns != null && this.keyColumns.size() > 0 ? (String[])this.keyColumns.toArray(new String[0]) : new String[0];
    }

    public void setKeyColumns(String keyColumn) {
        if (this.keyColumns == null) {
            this.keyColumns = new ArrayList();
            this.keyColumns.add(keyColumn);
        } else {
            this.keyColumns.add(keyColumn);
        }

    }

    public ResultMap getResultMap(Configuration configuration) {
        if (this.resultMap != null) {
            return this.resultMap;
        } else if (this.entityClassColumns != null && this.entityClassColumns.size() != 0) {
            List<ResultMapping> resultMappings = new ArrayList();
            Iterator var3 = this.entityClassColumns.iterator();

            while(var3.hasNext()) {
                EntityColumn entityColumn = (EntityColumn)var3.next();
                ResultMapping.Builder builder = new ResultMapping.Builder(configuration, entityColumn.getProperty(), entityColumn.getColumn(), entityColumn.getJavaType());
                if (entityColumn.getJdbcType() != null) {
                    builder.jdbcType(entityColumn.getJdbcType());
                }

                if (entityColumn.getTypeHandler() != null) {
                    try {
                        builder.typeHandler((TypeHandler)entityColumn.getTypeHandler().newInstance());
                    } catch (Exception var7) {
                        throw new MapperException(var7);
                    }
                }

                List<ResultFlag> flags = new ArrayList();
                if (entityColumn.isId()) {
                    flags.add(ResultFlag.ID);
                }

                builder.flags(flags);
                resultMappings.add(builder.build());
            }

            org.apache.ibatis.mapping.ResultMap.Builder builder = new org.apache.ibatis.mapping.ResultMap.Builder(configuration, "BaseMapperResultMap", this.entityClass, resultMappings, true);
            this.resultMap = builder.build();
            return this.resultMap;
        } else {
            return null;
        }
    }

    public void initPropertyMap() {
        this.propertyMap = new HashMap(this.getEntityClassColumns().size());
        Iterator var1 = this.getEntityClassColumns().iterator();

        while(var1.hasNext()) {
            EntityColumn column = (EntityColumn)var1.next();
            this.propertyMap.put(column.getProperty(), column);
        }

    }

    public Map<String, EntityColumn> getPropertyMap() {
        return this.propertyMap;
    }
}
