package com.travel.function.vo;


import com.travel.function.entity.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVo {
	private GoodsVo goods;
	private OrderInfo order;
}
