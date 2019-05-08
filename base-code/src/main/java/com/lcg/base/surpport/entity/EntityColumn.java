package com.lcg.base.surpport.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class EntityColumn {
    private EntityTable table;
    private String property;
    private String column;
    private Class<?> javaType;
    private JdbcType jdbcType;
    private Class<? extends TypeHandler<?>> typeHandler;
    private String sequenceName;
    private boolean id = false;
    private boolean uuid = false;
    private boolean identity = false;
    private String generator;
    private String orderBy;
    private boolean insertable = true;
    private boolean updatable = true;

    public EntityColumn() {
    }

    public EntityColumn(EntityTable table) {
        this.table = table;
    }

    public EntityTable getTable() {
        return this.table;
    }

    public void setTable(EntityTable table) {
        this.table = table;
    }

    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Class<?> getJavaType() {
        return this.javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public JdbcType getJdbcType() {
        return this.jdbcType;
    }

    public void setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
    }

    public Class<? extends TypeHandler<?>> getTypeHandler() {
        return this.typeHandler;
    }

    public void setTypeHandler(Class<? extends TypeHandler<?>> typeHandler) {
        this.typeHandler = typeHandler;
    }

    public String getSequenceName() {
        return this.sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public boolean isId() {
        return this.id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isUuid() {
        return this.uuid;
    }

    public void setUuid(boolean uuid) {
        this.uuid = uuid;
    }

    public boolean isIdentity() {
        return this.identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public String getGenerator() {
        return this.generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isInsertable() {
        return this.insertable;
    }

    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    public boolean isUpdatable() {
        return this.updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    public String getColumnEqualsHolder() {
        return this.getColumnEqualsHolder((String)null);
    }

    public String getColumnEqualsHolder(String entityName) {
        return this.column + " = " + this.getColumnHolder(entityName);
    }

    public String getColumnHolder() {
        return this.getColumnHolder((String)null);
    }

    public String getColumnHolder(String entityName) {
        return this.getColumnHolder(entityName, (String)null);
    }

    public String getColumnHolder(String entityName, String suffix) {
        return this.getColumnHolder(entityName, (String)null, (String)null);
    }

    public String getColumnHolderWithComma(String entityName, String suffix) {
        return this.getColumnHolder(entityName, suffix, ",");
    }

    public String getColumnHolder(String entityName, String suffix, String separator) {
        StringBuffer sb = new StringBuffer("#{");
        if (StringUtils.isNotEmpty(entityName)) {
            sb.append(entityName);
            sb.append(".");
        }

        sb.append(this.property);
        if (StringUtils.isNotEmpty(suffix)) {
            sb.append(suffix);
        }

        if (this.jdbcType != null) {
            sb.append(",jdbcType=");
            sb.append(this.jdbcType.toString());
        } else if (this.typeHandler != null) {
            sb.append(",typeHandler=");
            sb.append(this.typeHandler.getCanonicalName());
        } else if (!this.javaType.isArray()) {
            sb.append(",javaType=");
            sb.append(this.javaType.getCanonicalName());
        }

        sb.append("}");
        if (StringUtils.isNotEmpty(separator)) {
            sb.append(separator);
        }

        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            EntityColumn that = (EntityColumn)o;
            if (this.id != that.id) {
                return false;
            } else if (this.uuid != that.uuid) {
                return false;
            } else if (this.identity != that.identity) {
                return false;
            } else {
                if (this.table != null) {
                    if (!this.table.equals(that.table)) {
                        return false;
                    }
                } else if (that.table != null) {
                    return false;
                }

                if (this.property != null) {
                    if (!this.property.equals(that.property)) {
                        return false;
                    }
                } else if (that.property != null) {
                    return false;
                }

                label105: {
                    if (this.column != null) {
                        if (this.column.equals(that.column)) {
                            break label105;
                        }
                    } else if (that.column == null) {
                        break label105;
                    }

                    return false;
                }

                label98: {
                    if (this.javaType != null) {
                        if (this.javaType.equals(that.javaType)) {
                            break label98;
                        }
                    } else if (that.javaType == null) {
                        break label98;
                    }

                    return false;
                }

                if (this.jdbcType != that.jdbcType) {
                    return false;
                } else {
                    label90: {
                        if (this.typeHandler != null) {
                            if (this.typeHandler.equals(that.typeHandler)) {
                                break label90;
                            }
                        } else if (that.typeHandler == null) {
                            break label90;
                        }

                        return false;
                    }

                    if (this.sequenceName != null) {
                        if (!this.sequenceName.equals(that.sequenceName)) {
                            return false;
                        }
                    } else if (that.sequenceName != null) {
                        return false;
                    }

                    if (this.generator != null) {
                        if (!this.generator.equals(that.generator)) {
                            return false;
                        }
                    } else if (that.generator != null) {
                        return false;
                    }

                    boolean var10000;
                    label159: {
                        if (this.orderBy != null) {
                            if (this.orderBy.equals(that.orderBy)) {
                                break label159;
                            }
                        } else if (that.orderBy == null) {
                            break label159;
                        }

                        var10000 = false;
                        return var10000;
                    }

                    var10000 = true;
                    return var10000;
                }
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.table != null ? this.table.hashCode() : 0;
        result = 31 * result + (this.property != null ? this.property.hashCode() : 0);
        result = 31 * result + (this.column != null ? this.column.hashCode() : 0);
        result = 31 * result + (this.javaType != null ? this.javaType.hashCode() : 0);
        result = 31 * result + (this.jdbcType != null ? this.jdbcType.hashCode() : 0);
        result = 31 * result + (this.typeHandler != null ? this.typeHandler.hashCode() : 0);
        result = 31 * result + (this.sequenceName != null ? this.sequenceName.hashCode() : 0);
        result = 31 * result + (this.id ? 1 : 0);
        result = 31 * result + (this.uuid ? 1 : 0);
        result = 31 * result + (this.identity ? 1 : 0);
        result = 31 * result + (this.generator != null ? this.generator.hashCode() : 0);
        result = 31 * result + (this.orderBy != null ? this.orderBy.hashCode() : 0);
        return result;
    }
}
