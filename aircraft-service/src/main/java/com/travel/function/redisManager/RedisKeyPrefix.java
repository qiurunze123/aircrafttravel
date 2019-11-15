package com.travel.function.redisManager;

public interface RedisKeyPrefix {

    public int expireSeconds() ;

    public String getPrefix() ;

}
