package com.travel.function.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CustomerLogin {
    private Integer customerId;

    private String loginName;

    private String password;

    private Integer userStats;

    private Date modifiedTime;

}