package com.travel.web;

import com.alibaba.fastjson.JSON;
import com.travel.commons.utils.UUIDUtil;
import com.travel.function.dao.OrderInfoDao;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.function.redisManager.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

/**
 * @author 邱润泽 bullock
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
@Slf4j
public class AirCraftTest {

    @Autowired
    private OrderInfoDao dao;
//
//    @Autowired
//    private RedissonClient redissonClient;

    @Autowired
    private RedisClient redisClient;



    @Test
    public void redisLocktest()
    {
//        rrdisLock.put("qiurunze","GOOD",2000);
//        System.out.println(rrdisLock.get("qiurunze")+"=======redis======");
//        rrdisLock.put("chinago","chinago",2000);
//
//        rrdisLock.Lock("chinago12",100000);
//        if(rrdisLock.get("chinago12")==null){
//            System.out.println("========lock===========");
//        }
    }

    @Test
    public void customerTest() throws Exception {
//        CustomerLogin customerLogin = new CustomerLogin();
//        customerLogin.setCustomerId(100);
//        customerLogin.setLoginName("qiurunze");
//        customerLogin.setModifiedTime(new Date());
//        customerLogin.setPassword("123456");
//        customerLogin.setUserStats(0);
//        customerService.insertCustomerLogin(customerLogin);
    }

    @Test
    public void md5Test(){

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(1L);
        orderInfo.setGoodsName("ssss");
        orderInfo.setGoodsPrice(new BigDecimal(1));
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(222L);
        Long info  = dao.insertSelective(orderInfo);
        log.info(JSON.toJSONString(orderInfo.getId()));
    }

    @Test
    public void redisTest() throws Exception {
//        redisClient.set("qiurunze","帅气");
        System.out.println("======执行完毕 =====");
    }

    @Test
    public void uuidTest(){
        System.out.println(UUIDUtil.getUUid());
    }

    @Test
    public void redisNullTest(){
        System.out.println((MiaoShaUser)null);
    }


    @Test
    public void lambdaTest(){

        String[] strings = {"1","2"};
        String  a =  Arrays.stream(strings).filter(cookie -> cookie.equals("1")).findFirst().get();
        System.out.println("==================="+a+"====================");
    }

    @Test
    public void timeTest(){


        LocalDate localDate = LocalDate.now();
        Timestamp timestamp= Timestamp.valueOf(LocalDateTime.now());
        System.out.println(timestamp.getTime()+"=====================");
        System.out.println(System.currentTimeMillis()+"==================");

    }



}
