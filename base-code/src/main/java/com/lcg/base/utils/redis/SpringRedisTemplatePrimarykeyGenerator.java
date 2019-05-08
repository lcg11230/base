package com.lcg.base.utils.redis;

import com.lcg.base.utils.PrimarykeyGenerator;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by johnny on 2019/5/8.
 *
 * @author johnny
 */
public class SpringRedisTemplatePrimarykeyGenerator implements PrimarykeyGenerator {
    private RedisTemplate<String, Long> redisTemplate;

    public SpringRedisTemplatePrimarykeyGenerator() {
    }

    private String buildKey(String tableName) {
        return "ID_KE_" + tableName;
    }

    @Override
    public Long generateId(String tableName) {
        return this.incr(this.buildKey(tableName));
    }
    @Override
    public Long[] generateIds(String tableName, int size) {
        Long end = this.incrBy(this.buildKey(tableName), (long)size);
        Long begin = end - (long)size + 1L;
        Long[] ids = new Long[size];

        for(int i = 0; i < size; ++i) {
            ids[i] = begin + (long)i;
        }

        return ids;
    }

    private Long incr(String key) {
        return (Long)this.redisTemplate.execute((RedisCallback<Long>) (redisConnection) -> {
            byte[] keyByte = this.redisTemplate.getStringSerializer().serialize(key);
            return redisConnection.incr(keyByte);
        });
    }

    private Long incrBy(String key, long size) {
        return (Long)this.redisTemplate.execute((RedisCallback<Long>) (redisConnection) -> {
            byte[] keyByte = this.redisTemplate.getStringSerializer().serialize(key);
            return redisConnection.incrBy(keyByte, size);
        });
    }

    public RedisTemplate<String, Long> getRedisTemplate() {
        return this.redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
