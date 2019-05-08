package com.lcg.base.surpport;

import com.lcg.base.surpport.sqlprovider.BaseSqlProvider;
import com.lcg.base.surpport.sqlprovider.EmptyProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class MapperHelper {

    private final Map<String, Boolean> msIdSkip;
    private List<Class<?>> registerClass;
    private Map<Class<?>, BaseSqlProvider> registerMapper;
    private Map<String, BaseSqlProvider> msIdCache;
    private Config config;

    public MapperHelper() {
        this.msIdSkip = new HashMap();
        this.registerClass = new ArrayList();
        this.registerMapper = new ConcurrentHashMap();
        this.msIdCache = new HashMap();
        this.config = new Config();
    }

    public MapperHelper(Properties properties) {
        this();
        this.setProperties(properties);
    }

    public Config getConfig() {
        return this.config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    private BaseSqlProvider fromMapperClass(Class<?> mapperClass) {
        Method[] methods = mapperClass.getDeclaredMethods();
        Class<?> templateClass = null;
        Class<?> tempClass = null;
        Set<String> methodSet = new HashSet();
        Method[] var6 = methods;
        int var7 = methods.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            Method method = var6[var8];
            if (method.isAnnotationPresent(SelectProvider.class)) {
                SelectProvider provider = (SelectProvider) method.getAnnotation(SelectProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(InsertProvider.class)) {
                InsertProvider provider = (InsertProvider) method.getAnnotation(InsertProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(DeleteProvider.class)) {
                DeleteProvider provider = (DeleteProvider) method.getAnnotation(DeleteProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(UpdateProvider.class)) {
                UpdateProvider provider = (UpdateProvider) method.getAnnotation(UpdateProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            }

            if (templateClass == null) {
                templateClass = tempClass;
            } else if (templateClass != tempClass) {
                throw new MapperException("一个通用Mapper中只允许存在一个MapperTemplate子类!");
            }
        }

        if (templateClass == null || !BaseSqlProvider.class.isAssignableFrom(templateClass)) {
            templateClass = EmptyProvider.class;
        }

        var6 = null;

        BaseSqlProvider mapperTemplate;
        try {
            mapperTemplate = (BaseSqlProvider) templateClass.getConstructor(Class.class, MapperHelper.class).newInstance(mapperClass, this);
        } catch (Exception var12) {
            throw new MapperException("实例化MapperTemplate对象失败:" + var12.getMessage());
        }

        Iterator var14 = methodSet.iterator();

        while (var14.hasNext()) {
            String methodName = (String) var14.next();

            try {
                mapperTemplate.addMethodMap(methodName, templateClass.getMethod(methodName, MappedStatement.class));
            } catch (NoSuchMethodException var11) {
                throw new MapperException(templateClass.getCanonicalName() + "中缺少" + methodName + "方法!");
            }
        }

        return mapperTemplate;
    }

    public void registerMapper(Class<?> mapperClass) {
        if (!this.registerMapper.containsKey(mapperClass)) {
            this.registerClass.add(mapperClass);
            this.registerMapper.put(mapperClass, this.fromMapperClass(mapperClass));
        }

        Class<?>[] interfaces = mapperClass.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            Class[] var3 = interfaces;
            int var4 = interfaces.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Class<?> anInterface = var3[var5];
                this.registerMapper(anInterface);
            }
        }

    }

    public void registerMapper(String mapperClass) {
        try {
            this.registerMapper(Class.forName(mapperClass));
        } catch (ClassNotFoundException var3) {
            throw new MapperException("注册通用Mapper[" + mapperClass + "]失败，找不到该通用Mapper!");
        }
    }

    public boolean isMapperMethod(String msId) {
        if (this.msIdSkip.get(msId) != null) {
            return (Boolean) this.msIdSkip.get(msId);
        } else {
            Iterator var2 = this.registerMapper.entrySet().iterator();

            Map.Entry entry;
            do {
                if (!var2.hasNext()) {
                    this.msIdSkip.put(msId, false);
                    return false;
                }

                entry = (Map.Entry) var2.next();
            } while (!((BaseSqlProvider) entry.getValue()).supportMethod(msId));

            this.msIdSkip.put(msId, true);
            this.msIdCache.put(msId, (BaseSqlProvider) entry.getValue());
            return true;
        }
    }

    public boolean isExtendCommonMapper(Class<?> mapperInterface) {
        Iterator var2 = this.registerClass.iterator();

        Class mapperClass;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            mapperClass = (Class) var2.next();
        } while (!mapperClass.isAssignableFrom(mapperInterface));

        return true;
    }

    public void setSqlSource(MappedStatement ms) {
        BaseSqlProvider mapperTemplate = (BaseSqlProvider) this.msIdCache.get(ms.getId());

        try {
            if (mapperTemplate != null) {
                mapperTemplate.setSqlSource(ms);
            }

        } catch (Exception var4) {
            throw new MapperException(var4);
        }
    }

    public void setProperties(Properties properties) {
        this.config.setProperties(properties);
        String mapper = null;
        if (properties != null) {
            mapper = properties.getProperty("mappers");
        }

        if (StringUtils.isNotEmpty(mapper)) {
            String[] mappers = mapper.split(",");
            String[] var4 = mappers;
            int var5 = mappers.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String mapperClass = var4[var6];
                if (mapperClass.length() > 0) {
                    this.registerMapper(mapperClass);
                }
            }
        }

    }

    public void ifEmptyRegisterDefaultInterface() {
        if (this.registerClass.size() == 0) {
            this.registerMapper("com.ehsure.ttp.efx.core.mapper.BaseMapper");
        }

    }

    public void processConfiguration(Configuration configuration) {
        this.processConfiguration(configuration, (Class) null);
    }

    public void processConfiguration(Configuration configuration, Class<?> mapperInterface) {
        String prefix;
        if (mapperInterface != null) {
            prefix = mapperInterface.getCanonicalName();
        } else {
            prefix = "";
        }

        Iterator var4 = (new ArrayList(configuration.getMappedStatements())).iterator();

        while (var4.hasNext()) {
            Object object = var4.next();
            if (object instanceof MappedStatement) {
                MappedStatement ms = (MappedStatement) object;
                if (ms.getId().startsWith(prefix) && this.isMapperMethod(ms.getId()) && ms.getSqlSource() instanceof ProviderSqlSource) {
                    this.setSqlSource(ms);
                }
            }
        }

    }
}
