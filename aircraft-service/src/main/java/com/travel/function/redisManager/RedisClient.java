package com.travel.function.redisManager;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/10
 */
@Service
@Slf4j
public class RedisClient<T> {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key,value);
    }


    /**
     * 设置对象
     * */
    public <T> boolean set(RedisKeyPrefix prefix, String key, T value) {
        try {
            String str = beanToString(value);
            if(str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            int seconds =  prefix.expireSeconds();
            if(seconds <= 0) {
                redisTemplate.opsForValue().set(realKey, str);
            }else {
                redisTemplate.opsForValue().set(realKey, str,seconds,TimeUnit.SECONDS);
            }
            return true;
        }finally {
            log.info("***设置对象完成***");
        }
    }

    /**
     * 减少值
     * */
    public <T> Long decr(RedisKeyPrefix prefix, String key) {
        try {
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            redisTemplate.opsForValue().get(realKey);
            Long result = redisTemplate.opsForValue().increment(realKey,-1);
            return result;
        }finally {
            log.info("***减少对象完成***");
        }
    }

    /**
     * 获取当个对象
     * */
    public <T> T get(RedisKeyPrefix prefix, String key,  Class<T> clazz) {
        try {
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            String  str =redisTemplate.opsForValue().get(realKey);
            T t = stringToBean(str, clazz);
            return t;
        }finally {
            log.info("***获取对象完成***");
        }
    }

    /**
     * <p>
     * 通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1
     * </p>
     *
     * @param key
     * @return 加值后的结果
     */
    public Long incr(String key) {
        Long res = null;
        try {
            res = redisTemplate.opsForValue().increment(key,1);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            log.info("***增加对象完成***");
        }
        return res;
    }


    /**
     * 删除
     * */
    public boolean delete(RedisKeyPrefix prefix, String key) {
        try {
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return redisTemplate.delete(realKey);
        }finally {
            log.info("***减少对象完成***");
        }
    }

    public static <T> T stringToBean(String str, Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    public static <T> String beanToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }


}
