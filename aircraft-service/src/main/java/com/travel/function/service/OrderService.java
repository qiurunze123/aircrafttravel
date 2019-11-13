package com.travel.function.service;

import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.function.vo.GoodsVo;

/**
 * @author 邱润泽 bullock
 */
public interface OrderService {

    public MiaoShaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);

    public OrderInfo createOrder(MiaoShaUser user, GoodsVo goods);
}
