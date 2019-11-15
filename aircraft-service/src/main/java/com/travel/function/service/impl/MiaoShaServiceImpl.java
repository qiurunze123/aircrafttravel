package com.travel.function.service.impl;

import com.travel.commons.utils.MD5Util;
import com.travel.commons.utils.UUIDUtil;
import com.travel.function.dao.MiaoShaOrderDao;
import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.function.redisManager.RedisClient;
import com.travel.function.redisManager.keysbean.MiaoshaKey;
import com.travel.function.service.GoodsService;
import com.travel.function.service.MiaoshaService;
import com.travel.function.service.OrderService;
import com.travel.function.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 邱润泽 bullock
 */
@Service
@Slf4j
public class MiaoShaServiceImpl implements MiaoshaService {


    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoShaOrderDao miaoShaOrderDao;

    @Autowired
    RedisClient redisClient;

    @Transactional
    @Override
    public OrderInfo miaosha(MiaoShaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        goodsService.reduceStock(goods);
        //order_info maiosha_order
        return orderService.createOrder(user, goods);
    }

    @Override
    public int insertMiaoshaOrder(MiaoShaOrder miaoshaOrder) {
        return miaoShaOrderDao.insertSelective(miaoshaOrder);
    }

    @Override
    public MiaoShaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        return miaoShaOrderDao.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
    }

    @Override
    public String createMiaoshaPath(MiaoShaUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.getUUid() + "123456");
        log.info("createMiaoShaPath str:{}", str);
        redisClient.set(MiaoshaKey.getMiaoshaPath, "" + user.getNickname() + "_" + goodsId, str);
        return str;
    }

    @Override
    public boolean checkPath(MiaoShaUser user, long goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String pathOld = (String) redisClient.get(MiaoshaKey.getMiaoshaPath,
                "" + user.getNickname() + "_" + goodsId, String.class);
        return path.equals(pathOld);
    }
}
