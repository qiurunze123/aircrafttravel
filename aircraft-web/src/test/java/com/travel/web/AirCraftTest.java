package com.travel.web;

import com.alibaba.fastjson.JSON;
import com.travel.commons.redisManager.RedisService;
import com.travel.function.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 邱润泽 bullock
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class AirCraftTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService rrdisLock;

    @Test
    public void userTest(){
        System.out.println("获取user ====== "+JSON.toJSONString(userService.getUser(1)));
    }

    @Test
    public void redisLocktest()
    {
        rrdisLock.put("qiurunze","GOOD",2000);
        System.out.println(rrdisLock.get("qiurunze")+"=======redis======");
    }
}
