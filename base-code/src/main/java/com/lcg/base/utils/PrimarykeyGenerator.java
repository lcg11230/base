package com.lcg.base.utils;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public interface PrimarykeyGenerator {
    String PREFIX = "ID_KE_";

    Long generateId(String var1);

    Long[] generateIds(String var1, int var2);
}
