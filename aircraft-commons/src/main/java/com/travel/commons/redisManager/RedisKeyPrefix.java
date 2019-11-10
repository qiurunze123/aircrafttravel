package com.travel.commons.redisManager;

public interface RedisKeyPrefix {

    public int expireSeconds() ;

    public String getPrefix() ;

}
