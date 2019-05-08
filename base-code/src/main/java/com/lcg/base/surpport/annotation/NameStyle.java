package com.lcg.base.surpport.annotation;

import com.lcg.base.surpport.Style;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NameStyle {
    Style value() default Style.normal;
}
