package com.travel.web.controller;

import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.redisManager.RedisClient;
import com.travel.service.GoodsService;
import com.travel.service.MiaoshaService;
import com.travel.service.OrderService;
import com.travel.vo.GoodsVo;
import com.travel.vo.OrderDetailVo;
import com.travel.vo.OrderInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController {

	@Autowired
	MiaoshaService miaoshaService;
	@Autowired
	RedisClient redisClient;
	@Autowired
	OrderService orderService;
	@Autowired
	GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public ResultGeekQ<OrderDetailVo> info( MiaoShaUser user,
										   @RequestParam("orderId") long orderId) {
    	ResultGeekQ result = ResultGeekQ.build();
    	try {
			if (user == null) {
				result.withError(ResultStatus.SESSION_ERROR);
				return result;
			}
			ResultGeekQ<OrderInfoVo> orderR = orderService.getOrderById(orderId);
			if(!ResultGeekQ.isSuccess(orderR)){
				result.withError(orderR.getCode(),orderR.getMessage());
				return result;
			}
			Long goodsId = orderR.getData().getGoodsId();
			ResultGeekQ<GoodsVo> goodsR = goodsService.goodsVoByGoodId(goodsId);
			if(!ResultGeekQ.isSuccess(goodsR)){
				result.withError(orderR.getCode(),orderR.getMessage());
				return result;
			}
			OrderDetailVo vo = new OrderDetailVo();
			vo.setOrder(orderR.getData());
			vo.setGoods(goodsR.getData());
			result.setData(vo);
		}catch (Exception e){
    		log.error("查询明细订单失败 error:{}",e);
			result.error(ResultStatus.SYSTEM_ERROR);
			return result;
		}
    	return result;
    }
    
}
