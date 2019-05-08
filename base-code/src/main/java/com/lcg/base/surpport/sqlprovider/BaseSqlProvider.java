package com.lcg.base.surpport.sqlprovider;

import com.lcg.base.surpport.MapperException;
import com.lcg.base.surpport.MapperHelper;
import com.lcg.base.surpport.MultipleJdbc3KeyGenerator;
import com.lcg.base.surpport.SelectKeyGenerator;
import com.lcg.base.surpport.entity.EntityColumn;
import com.lcg.base.surpport.entity.EntityHelper;
import com.lcg.base.surpport.entity.EntityTable;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public abstract class BaseSqlProvider {
    private static final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    protected Map<String, Method> methodMap = new HashMap();
    protected Map<String, Class<?>> entityClassMap = new HashMap();
    protected Class<?> mapperClass;
    protected MapperHelper mapperHelper;

    public BaseSqlProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        this.mapperClass = mapperClass;
        this.mapperHelper = mapperHelper;
    }

    public static Class<?> getMapperClass(String msId) {
        if (msId.indexOf(".") == -1) {
            throw new MapperException("当前MappedStatement的id=" + msId + ",不符合MappedStatement的规则!");
        } else {
            String mapperClassStr = msId.substring(0, msId.lastIndexOf("."));

            try {
                return Class.forName(mapperClassStr);
            } catch (ClassNotFoundException var3) {
                return null;
            }
        }
    }

    public static String getMethodName(MappedStatement ms) {
        return getMethodName(ms.getId());
    }

    public static String getMethodName(String msId) {
        return msId.substring(msId.lastIndexOf(".") + 1);
    }

    public String dynamicSQL(Object record) {
        return "dynamicSQL";
    }

    public void addMethodMap(String methodName, Method method) {
        this.methodMap.put(methodName, method);
    }

    public String getUUID() {
        return this.mapperHelper.getConfig().getUUID();
    }

    public String getIDENTITY() {
        return this.mapperHelper.getConfig().getIDENTITY();
    }

    public boolean isBEFORE() {
        return this.mapperHelper.getConfig().isBEFORE();
    }

    public boolean isNotEmpty() {
        return this.mapperHelper.getConfig().isNotEmpty();
    }

    public boolean isCheckExampleEntityClass() {
        return this.mapperHelper.getConfig().isCheckExampleEntityClass();
    }

    public boolean supportMethod(String msId) {
        Class<?> mapperClass = getMapperClass(msId);
        if (mapperClass != null && this.mapperClass.isAssignableFrom(mapperClass)) {
            String methodName = getMethodName(msId);
            return this.methodMap.get(methodName) != null;
        } else {
            return false;
        }
    }

    protected void setResultType(MappedStatement ms, Class<?> entityClass) {
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        List<ResultMap> resultMaps = new ArrayList();
        resultMaps.add(entityTable.getResultMap(ms.getConfiguration()));
        MetaObject metaObject = SystemMetaObject.forObject(ms);
        metaObject.setValue("resultMaps", Collections.unmodifiableList(resultMaps));
    }

    protected void setSqlSource(MappedStatement ms, SqlSource sqlSource) {
        MetaObject msObject = SystemMetaObject.forObject(ms);
        msObject.setValue("sqlSource", sqlSource);
        KeyGenerator keyGenerator = ms.getKeyGenerator();
        if (keyGenerator instanceof Jdbc3KeyGenerator) {
            msObject.setValue("keyGenerator", new MultipleJdbc3KeyGenerator());
        }

    }

    private void checkCache(MappedStatement ms) throws Exception {
        if (ms.getCache() == null) {
            String nameSpace = ms.getId().substring(0, ms.getId().lastIndexOf("."));

            Cache cache;
            try {
                cache = ms.getConfiguration().getCache(nameSpace);
            } catch (IllegalArgumentException var5) {
                return;
            }

            if (cache != null) {
                MetaObject metaObject = SystemMetaObject.forObject(ms);
                metaObject.setValue("cache", cache);
            }
        }

    }

    public void setSqlSource(MappedStatement ms) throws Exception {
        if (this.mapperClass == getMapperClass(ms.getId())) {
            throw new MapperException("请不要配置或扫描通用Mapper接口类：" + this.mapperClass);
        } else {
            Method method = (Method) this.methodMap.get(getMethodName(ms));

            try {
                if (method.getReturnType() == Void.TYPE) {
                    method.invoke(this, ms);
                } else if (SqlNode.class.isAssignableFrom(method.getReturnType())) {
                    SqlNode sqlNode = (SqlNode) method.invoke(this, ms);
                    DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), sqlNode);
                    this.setSqlSource(ms, dynamicSqlSource);
                } else {
                    if (!String.class.equals(method.getReturnType())) {
                        throw new MapperException("自定义Mapper方法返回类型错误,可选的返回类型为void,SqlNode,String三种!");
                    }

                    String xmlSql = (String) method.invoke(this, ms);
                    SqlSource sqlSource = this.createSqlSource(ms, xmlSql);
                    this.setSqlSource(ms, sqlSource);
                }

                this.checkCache(ms);
            } catch (IllegalAccessException var5) {
                throw new MapperException(var5);
            } catch (InvocationTargetException var6) {
                throw new MapperException((Throwable) (var6.getTargetException() != null ? var6.getTargetException() : var6));
            }
        }
    }

    public SqlSource createSqlSource(MappedStatement ms, String xmlSql) {
        return languageDriver.createSqlSource(ms.getConfiguration(), "<script>\n\t" + xmlSql + "</script>", (Class) null);
    }

    public Class<?> getEntityClass(MappedStatement ms) {
        String msId = ms.getId();
        if (this.entityClassMap.containsKey(msId)) {
            return (Class) this.entityClassMap.get(msId);
        } else {
            Class<?> mapperClass = getMapperClass(msId);
            Type[] types = mapperClass.getGenericInterfaces();
            Type[] var5 = types;
            int var6 = types.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                Type type = var5[var7];
                if (type instanceof ParameterizedType) {
                    ParameterizedType t = (ParameterizedType) type;
                    if (t.getRawType() == this.mapperClass || this.mapperClass.isAssignableFrom((Class) t.getRawType())) {
                        Class<?> returnType = (Class) t.getActualTypeArguments()[0];
                        EntityHelper.initEntityNameMap(returnType, this.mapperHelper.getConfig());
                        this.entityClassMap.put(msId, returnType);
                        return returnType;
                    }
                }
            }

            throw new MapperException("无法获取Mapper<T>泛型类型:" + msId);
        }
    }


    protected String getSeqNextVal(EntityColumn column) {
        return MessageFormat.format(this.mapperHelper.getConfig().getSeqFormat(), column.getSequenceName(), column.getColumn(), column.getProperty(), column.getTable().getName());
    }

    protected String tableName(Class<?> entityClass) {
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        String prefix = entityTable.getPrefix();
        if (StringUtils.isEmpty(prefix)) {
            prefix = this.mapperHelper.getConfig().getPrefix();
        }

        return StringUtils.isNotEmpty(prefix) ? prefix + "." + entityTable.getName() : entityTable.getName();
    }
    protected void newSelectKeyMappedStatement(MappedStatement ms, EntityColumn column) {
        String keyId = ms.getId() + "!selectKey";
        if (!ms.getConfiguration().hasKeyGenerator(keyId)) {
            Class<?> entityClass = this.getEntityClass(ms);
            Configuration configuration = ms.getConfiguration();
            Boolean executeBefore = this.isBEFORE();
            String IDENTITY = column.getGenerator() != null && !column.getGenerator().equals("") ? column.getGenerator() : this.getIDENTITY();
            Object keyGenerator;
            if (IDENTITY.equalsIgnoreCase("JDBC")) {
                keyGenerator = new Jdbc3KeyGenerator();
            } else {
                SqlSource sqlSource = new RawSqlSource(configuration, IDENTITY, entityClass);
                org.apache.ibatis.mapping.MappedStatement.Builder statementBuilder = new org.apache.ibatis.mapping.MappedStatement.Builder(configuration, keyId, sqlSource, SqlCommandType.SELECT);
                statementBuilder.resource(ms.getResource());
                statementBuilder.fetchSize((Integer)null);
                statementBuilder.statementType(StatementType.STATEMENT);
                statementBuilder.keyGenerator(new NoKeyGenerator());
                statementBuilder.keyProperty(column.getProperty());
                statementBuilder.keyColumn((String)null);
                statementBuilder.databaseId((String)null);
                statementBuilder.lang(configuration.getDefaultScriptingLanuageInstance());
                statementBuilder.resultOrdered(false);
                statementBuilder.resulSets((String)null);
                statementBuilder.timeout(configuration.getDefaultStatementTimeout());
                List<ParameterMapping> parameterMappings = new ArrayList();
                org.apache.ibatis.mapping.ParameterMap.Builder inlineParameterMapBuilder = new org.apache.ibatis.mapping.ParameterMap.Builder(configuration, statementBuilder.id() + "-Inline", entityClass, parameterMappings);
                statementBuilder.parameterMap(inlineParameterMapBuilder.build());
                List<ResultMap> resultMaps = new ArrayList();
                org.apache.ibatis.mapping.ResultMap.Builder inlineResultMapBuilder = new org.apache.ibatis.mapping.ResultMap.Builder(configuration, statementBuilder.id() + "-Inline", column.getJavaType(), new ArrayList(), (Boolean)null);
                resultMaps.add(inlineResultMapBuilder.build());
                statementBuilder.resultMaps(resultMaps);
                statementBuilder.resultSetType((ResultSetType)null);
                statementBuilder.flushCacheRequired(false);
                statementBuilder.useCache(false);
                statementBuilder.cache((Cache)null);
                MappedStatement statement = statementBuilder.build();

                try {
                    configuration.addMappedStatement(statement);
                } catch (Exception var20) {
                    ;
                }

                MappedStatement keyStatement = configuration.getMappedStatement(keyId, false);
                keyGenerator = new SelectKeyGenerator(keyStatement, executeBefore);

                try {
                    configuration.addKeyGenerator(keyId, (KeyGenerator)keyGenerator);
                } catch (Exception var19) {
                    ;
                }
            }

            try {
                MetaObject msObject = SystemMetaObject.forObject(ms);
                msObject.setValue("keyGenerator", keyGenerator);
                msObject.setValue("keyProperties", column.getTable().getKeyProperties());
                msObject.setValue("keyColumns", column.getTable().getKeyColumns());
            } catch (Exception var18) {
                ;
            }

        }
    }


}
