package com.travel.web;

import com.travel.function.entity.User;
import com.travel.function.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AirCraftApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void getUser(){
       User user =  userService.getUser(1);
        System.out.println(user.getAddress());
    }

}
