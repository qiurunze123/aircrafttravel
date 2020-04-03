package com.travel.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CustomerLoginVo {
    private Integer customerId;

    private String loginName;

    private String password;

    private Integer userStats;

    private Date modifiedTime;

}