package com.travel.web;

import com.alibaba.fastjson.JSON;
import com.travel.function.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLOutput;

/**
 * @author 邱润泽 bullock
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class AirCraftTest {

    @Autowired
    private UserService userService;

    @Test
    public void userTest(){
        System.out.println("获取user ====== "+JSON.toJSONString(userService.getUser(1)));
    }
}
