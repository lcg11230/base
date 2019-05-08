package com.lcg.base.surpport;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class SimpleTypeUtil {
    private static final Set<Class<?>> SIMPLE_TYPE_SET = new HashSet();

    public SimpleTypeUtil() {
    }

    public static void registerSimpleType(Class<?> clazz) {
        SIMPLE_TYPE_SET.add(clazz);
    }

    public static void registerSimpleType(String classes) {
        if (StringUtils.isNotEmpty(classes)) {
            String[] cls = classes.split(",");
            String[] var2 = cls;
            int var3 = cls.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String c = var2[var4];

                try {
                    SIMPLE_TYPE_SET.add(Class.forName(c));
                } catch (ClassNotFoundException var7) {
                    throw new MapperException("注册类型出错:" + c, var7);
                }
            }
        }

    }

    public static boolean isSimpleType(Class<?> clazz) {
        return SIMPLE_TYPE_SET.contains(clazz);
    }

    static {
        SIMPLE_TYPE_SET.add(byte[].class);
        SIMPLE_TYPE_SET.add(String.class);
        SIMPLE_TYPE_SET.add(Byte.class);
        SIMPLE_TYPE_SET.add(Short.class);
        SIMPLE_TYPE_SET.add(Character.class);
        SIMPLE_TYPE_SET.add(Integer.class);
        SIMPLE_TYPE_SET.add(Long.class);
        SIMPLE_TYPE_SET.add(Float.class);
        SIMPLE_TYPE_SET.add(Double.class);
        SIMPLE_TYPE_SET.add(Boolean.class);
        SIMPLE_TYPE_SET.add(Date.class);
        SIMPLE_TYPE_SET.add(Class.class);
        SIMPLE_TYPE_SET.add(BigInteger.class);
        SIMPLE_TYPE_SET.add(BigDecimal.class);
    }
}
