package com.travel.function.service;

import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.function.vo.GoodsVo;

/**
 * @author 邱润泽 bullock
 */
public interface MiaoshaService {

    public OrderInfo miaosha(MiaoShaUser user, GoodsVo goods);

    public int insertMiaoshaOrder(MiaoShaOrder miaoshaOrder);

    public MiaoShaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);

    String createMiaoshaPath(MiaoShaUser user, long goodsId);

    boolean checkPath(MiaoShaUser user, long goodsId, String path);
}
