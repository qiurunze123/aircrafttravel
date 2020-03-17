package com.travel.function.service.impl;

import com.travel.commons.enums.CustomerConstant;
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

import java.util.List;

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

    @Transactional(rollbackFor = Exception.class)
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

    @Override
    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoShaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {//秒杀成功
            return order.getId();
        } else {
            //此商品的秒杀已经结束，但是可能订单还在生成中
            //获取所有的秒杀订单, 判断订单数量和参与秒杀的商品数量
            List<MiaoShaOrder> orders = this.getAllMiaoshaOrdersByGoodsId(goodsId);
            if (orders == null) {
                return CustomerConstant.MS_ING;//订单还在生成中
            } else {//判断是否有此用户的订单
                MiaoShaOrder orderIsGet = get(orders, userId);
                if (orderIsGet != null) {//如果有，则说明秒杀成功
                    return orderIsGet.getOrderId();
                } else {//秒杀失败
                    return CustomerConstant.MS_F;
                }
            }
        }
    }

    @Override
    public List<MiaoShaOrder> getAllMiaoshaOrdersByGoodsId(long goodsId) {
        return miaoShaOrderDao.listByGoodsId(goodsId);
    }


    private MiaoShaOrder get(List<MiaoShaOrder> orders, Long userId) {
        if (orders == null || orders.size() <= 0) {
            return null;
        }
        for (MiaoShaOrder order : orders) {
            if (order.getUserId().equals(userId)) {
                return order;
            }
        }
        return null;
    }
}
