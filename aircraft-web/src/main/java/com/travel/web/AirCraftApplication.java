package com.travel.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.travel.function","com.travel","com.travel.vo"})
@MapperScan("com.travel.function.dao")
public class AirCraftApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirCraftApplication.class, args);
    }

}
