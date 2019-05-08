package com.lcg.base.utils;

import com.lcg.base.utils.redis.SpringRedisTemplatePrimarykeyGenerator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
@Component
public class PrimarykeyGenerateHelper implements ApplicationContextAware {
    private ApplicationContext appContext;

    public PrimarykeyGenerateHelper() {
    }

    public PrimarykeyGenerator getGenerator(String key) {
        return key != null && !key.equals("") ? (PrimarykeyGenerator) this.appContext.getBean(key, PrimarykeyGenerator.class) : (PrimarykeyGenerator) this.appContext.getBean(SpringRedisTemplatePrimarykeyGenerator.class);
    }

    public PrimarykeyGenerator getGenerator() {
        return this.getGenerator((String) null);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }
}
