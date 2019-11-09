package com.travel.web;

import com.alibaba.fastjson.JSON;
import com.travel.commons.redisManager.RedisService;
import com.travel.commons.utils.MD5Util;
import com.travel.function.entity.CustomerLogin;
import com.travel.function.service.CustomerService;
import com.travel.function.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

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

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CustomerService customerService;

    @Test
    public void userTest(){
        System.out.println("获取user ====== "+JSON.toJSONString(userService.getUser(1)));
    }

    @Test
    public void redisLocktest()
    {
        rrdisLock.put("qiurunze","GOOD",2000);
        System.out.println(rrdisLock.get("qiurunze")+"=======redis======");
        rrdisLock.put("chinago","chinago",2000);

        rrdisLock.Lock("chinago12",100000);
        if(rrdisLock.get("chinago12")==null){
            System.out.println("========lock===========");
        }
    }

    @Test
    public void customerTest() throws Exception {
        CustomerLogin customerLogin = new CustomerLogin();
        customerLogin.setCustomerId(100);
        customerLogin.setLoginName("qiurunze");
        customerLogin.setModifiedTime(new Date());
        customerLogin.setPassword("123456");
        customerLogin.setUserStats(0);
        customerService.insertCustomerLogin(customerLogin);
    }

    @Test
    public void md5Test(){

       String passwd =  MD5Util.inputPassToDbPass("111111","1a2b3c4d");
       System.out.println(passwd);
    }
}
