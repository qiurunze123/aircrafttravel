package com.travel.function.service.impl;

import com.travel.function.dao.MiaoShaOrderDao;
import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.function.service.MiaoshaService;
import com.travel.function.service.OrderInfoService;
import com.travel.function.service.OrderService;
import com.travel.function.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author 邱润泽 bullock
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    MiaoshaService miaoshaService;

    @Override
    public MiaoShaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        return miaoshaService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
    }

    @Override
    public OrderInfo createOrder(MiaoShaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderInfoService.insertSelective(orderInfo);
        MiaoShaOrder miaoshaOrder = new MiaoShaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(user.getId());
        miaoshaService.insertMiaoshaOrder(miaoshaOrder);
        return orderInfo;
    }


}
