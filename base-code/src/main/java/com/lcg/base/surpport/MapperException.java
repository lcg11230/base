package com.lcg.base.surpport;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class MapperException extends RuntimeException {
    public MapperException() {
    }

    public MapperException(String message) {
        super(message);
    }

    public MapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapperException(Throwable cause) {
        super(cause);
    }
}
