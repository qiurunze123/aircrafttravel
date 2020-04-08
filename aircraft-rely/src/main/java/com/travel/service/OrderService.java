package com.travel.service;


import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.vo.GoodsVo;
import com.travel.vo.MiaoShaOrderVo;
import com.travel.vo.MiaoShaUserVo;
import com.travel.vo.OrderInfoVo;

/**
 * @author 邱润泽 bullock
 */
public interface OrderService {

    public ResultGeekQ<MiaoShaOrderVo> getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId);

    public ResultGeekQ<OrderInfoVo> createOrder(MiaoShaUserVo user, GoodsVo goods);

    public ResultGeekQ<OrderInfoVo> getOrderById(Long orderId);

}
