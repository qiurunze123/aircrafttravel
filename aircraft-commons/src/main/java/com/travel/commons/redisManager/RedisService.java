package com.travel.commons.redisManager;

/**
 * @author 邱润泽 bullock
 */
public interface RedisService {

    public void put(String key, Object value, long seconds);

    public Object get(String key);
}
