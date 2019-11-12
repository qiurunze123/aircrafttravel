package com.travel.function.vo;

import com.travel.function.entity.Goods;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class GoodsVo extends Goods {
	private BigDecimal miaoshaPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
}
