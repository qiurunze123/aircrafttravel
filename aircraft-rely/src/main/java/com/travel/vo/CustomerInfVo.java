package com.travel.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Data
public class CustomerInfVo {
    private Integer customerInfId;

    private Integer customerId;

    private String customerName;

    private Byte identityCardType;

    private String identityCardNo;

    private Integer mobilePhone;

    private String customerEmail;

    private String gender;

    private Integer userPoint;

    private Date registerTime;

    private Date birthday;

    private Integer customerLevel;

    private BigDecimal userMoney;

    private Date modifiedTime;

}