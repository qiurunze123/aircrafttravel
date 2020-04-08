package com.travel.function.service.impl;

import com.travel.commons.enums.CustomerConstant;
import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.function.exception.DeductStockException;
import com.travel.function.logic.MiaoShaLogic;
import com.travel.function.redisManager.RedisClient;
import com.travel.function.redisManager.RedisKeyPrefix;
import com.travel.function.redisManager.keysbean.GoodsKey;
import com.travel.service.OrderService;
import com.travel.vo.GoodsVo;
import com.travel.vo.MiaoShaOrderVo;
import com.travel.vo.MiaoShaUserVo;
import com.travel.vo.OrderInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.travel.commons.enums.ResultStatus.MIAOSHA_DEDUCT_FAIL;

/**
 * @author 邱润泽 bullock
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MiaoShaLogic mSLogic;

    @Autowired
    private RedisClient redisClient;


    @Override
    public ResultGeekQ<MiaoShaOrderVo> getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId) {

        ResultGeekQ resultGeekQ = ResultGeekQ.build();
        try{
            MiaoShaOrder mSorder = mSLogic.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
            //如果为null 则无订单 返回订单已存在
            if(mSorder != null){
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.GOOD_EXIST);
                return resultGeekQ;
            }
            return resultGeekQ;
        }catch(Exception e){
            log.error("***getMiaoshaOrderByUserIdGoodsId***fail",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
            return resultGeekQ;
        }
    }


    @Override
    public ResultGeekQ<OrderInfoVo> createOrder(MiaoShaUserVo user, GoodsVo goods) {
        ResultGeekQ resultGeekQ = ResultGeekQ.build();
        try{
            MiaoShaUser mSuser = new MiaoShaUser();
            BeanUtils.copyProperties(user,mSuser);
            OrderInfo info = mSLogic.createOrder(mSuser, goods);
            if(info == null){
                info = new OrderInfo();
            }
            OrderInfoVo infoVo = new OrderInfoVo();
            BeanUtils.copyProperties(info,infoVo);
            resultGeekQ.setData(infoVo);
            return resultGeekQ;
        }catch(Exception e){
            log.error("***createOrder***fail",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
            return resultGeekQ;
        }
    }

    @Override
    public ResultGeekQ<OrderInfoVo> getOrderById(Long orderId) {
        ResultGeekQ resultGeekQ = ResultGeekQ.build();
        try{
            OrderInfo info = mSLogic.getOrderById(orderId);
            if(info == null){
                info = new OrderInfo();
            }
            OrderInfoVo infoVo = new OrderInfoVo();
            BeanUtils.copyProperties(info,infoVo);
            resultGeekQ.setData(infoVo);
            return resultGeekQ;
        }catch(Exception e){
            log.error("***getOrderById***fail",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
            return resultGeekQ;
        }
    }

}
