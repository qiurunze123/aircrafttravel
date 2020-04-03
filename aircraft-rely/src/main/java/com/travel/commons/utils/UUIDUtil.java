package com.travel.commons.utils;

import java.util.UUID;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/10
 */
public class UUIDUtil {

    public static String getUUid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
