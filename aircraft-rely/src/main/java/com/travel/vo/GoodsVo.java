package com.travel.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class GoodsVo {


    private BigDecimal miaoshaPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

    private Long id;

    private String goodsName;

    private String goodsTitle;

    private String goodsImg;

    private BigDecimal goodsPrice;

    private Integer goodsStock;

    private String goodsDetail;
}
