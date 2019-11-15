package com.travel.function.redisManager.keysbean;

import com.travel.function.redisManager.BasePrefix;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/10
 */
public class MiaoShaUserKey extends BasePrefix {
    public static final int TOKEN_EXPIRE = 3600 *2;
    public static MiaoShaUserKey token = new MiaoShaUserKey(TOKEN_EXPIRE,"tk") ;
    public static MiaoShaUserKey getByNickName = new MiaoShaUserKey(0, "nickName");

    public MiaoShaUserKey(int expireSeconds ,String prefix) {
        super(expireSeconds,prefix);
    }
}