package com.lcg.base.utils.snowflake;

import java.util.UUID;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class IdWorker {
    private static Sequence worker = new Sequence();

    public IdWorker() {
    }

    public static long getId() {
        return worker.nextId();
    }

    public static synchronized String get32UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
