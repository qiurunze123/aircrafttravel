package com.travel.web.controller;

import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.function.redisManager.RedisClient;
import com.travel.function.vo.GoodsVo;
import com.travel.function.vo.OrderDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public ResultGeekQ<OrderDetailVo> info(Model model, MiaoShaUser user,
										   @RequestParam("orderId") long orderId) {
    	ResultGeekQ result = ResultGeekQ.build();
    	try {
			if (user == null) {
				result.withError(ResultStatus.SESSION_ERROR);
				return result;
			}
			OrderInfo order = orderService.getOrderById(orderId);
			if (order == null) {
				return result.error(ResultStatus.ORDER_NOT_EXIST);
			}
			long goodsId = order.getGoodsId();
			GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
			OrderDetailVo vo = new OrderDetailVo();
			vo.setOrder(order);
			vo.setGoods(goods);
			result.setData(vo);
		}catch (Exception e){
    		log.error("查询明细订单失败 error:{}",e);
			result.error(ResultStatus.SYSTEM_ERROR);
			return result;
		}
    	return result;
    }
    
}
