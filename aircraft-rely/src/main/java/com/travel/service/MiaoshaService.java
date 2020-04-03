package com.travel.service;


import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.vo.GoodsVo;
import com.travel.vo.MiaoShaOrderVo;
import com.travel.vo.MiaoShaUserVo;
import com.travel.vo.OrderInfoVo;

import java.util.List;

/**
 * @author 邱润泽 bullock
 */
public interface MiaoshaService {

    public ResultGeekQ<OrderInfoVo> miaosha(MiaoShaUserVo user, GoodsVo goods);

    public ResultGeekQ<Integer> insertMiaoshaOrder(MiaoShaOrderVo miaoshaOrder);

    public ResultGeekQ<MiaoShaOrderVo> getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId);

    public ResultGeekQ<String> createMiaoshaPath(MiaoShaUserVo user, Long goodsId);

    public ResultGeekQ<Long> getMiaoshaResult(Long userId, Long goodsId);

}
