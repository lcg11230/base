package com.lcg.base.surpport.spring;

import com.lcg.base.surpport.MapperHelper;
import com.lcg.base.surpport.Marker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Properties;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class MapperScannerConfigurer extends org.mybatis.spring.mapper.MapperScannerConfigurer {
    private MapperHelper mapperHelper = new MapperHelper();

    public MapperScannerConfigurer() {
    }

    @Override
    public void setMarkerInterface(Class<?> superClass) {
        super.setMarkerInterface(superClass);
        if (Marker.class.isAssignableFrom(superClass)) {
            this.mapperHelper.registerMapper(superClass);
        }

    }

    public MapperHelper getMapperHelper() {
        return this.mapperHelper;
    }

    public void setMapperHelper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }

    public void setProperties(Properties properties) {
        this.mapperHelper.setProperties(properties);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        super.postProcessBeanDefinitionRegistry(registry);
        this.mapperHelper.ifEmptyRegisterDefaultInterface();
        String[] names = registry.getBeanDefinitionNames();
        String[] var4 = names;
        int var5 = names.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            String name = var4[var6];
            BeanDefinition beanDefinition = registry.getBeanDefinition(name);
            if (beanDefinition instanceof GenericBeanDefinition) {
                GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinition;
                if (StringUtils.isNotEmpty(definition.getBeanClassName()) && definition.getBeanClassName().equals("org.mybatis.spring.mapper.MapperFactoryBean")) {
                    definition.setBeanClass(MapperFactoryBean.class);
                    definition.getPropertyValues().add("mapperHelper", this.mapperHelper);
                }
            }
        }

    }
}
