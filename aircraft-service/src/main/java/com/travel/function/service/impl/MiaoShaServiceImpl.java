package com.travel.function.service.impl;

import com.alibaba.fastjson.JSON;
import com.travel.commons.enums.CustomerConstant;
import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.commons.utils.MD5Util;
import com.travel.commons.utils.UUIDUtil;
import com.travel.function.dao.MiaoShaOrderDao;
import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.function.logic.MiaoShaLogic;
import com.travel.function.redisManager.RedisClient;
import com.travel.function.redisManager.keysbean.MiaoshaKey;
import com.travel.service.GoodsService;
import com.travel.service.MiaoshaService;
import com.travel.service.OrderService;
import com.travel.vo.GoodsVo;
import com.travel.vo.MiaoShaOrderVo;
import com.travel.vo.MiaoShaUserVo;
import com.travel.vo.OrderInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private MiaoShaLogic mSLogic;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultGeekQ<OrderInfoVo> miaosha(MiaoShaUserVo user, GoodsVo goods) {

        ResultGeekQ<OrderInfoVo> resultGeekQ = ResultGeekQ.build();
        try{
            //减库存 下订单 写入秒杀订单
            ResultGeekQ<Boolean> result = goodsService.reduceStock(goods);
            if(!ResultGeekQ.isSuccess(result)){
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_REDUCE_FAIL);
                return resultGeekQ;
            }
            MiaoShaUser Muser =  new MiaoShaUser();
            BeanUtils.copyProperties(user,Muser);
            OrderInfo orderInfo = mSLogic.createOrder(Muser, goods);
            OrderInfoVo orderInfoVo = new OrderInfoVo();
            BeanUtils.copyProperties(orderInfo,orderInfoVo);
            resultGeekQ.setData(orderInfoVo);
            return resultGeekQ;
        }catch(Exception e){
            log.error("***秒杀下订单失败*** error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return resultGeekQ;
        }
    }

    @Override
    public ResultGeekQ<Integer> insertMiaoshaOrder(MiaoShaOrderVo vo) {
        ResultGeekQ<Integer> resultGeekQ = ResultGeekQ.build();
        try{
            MiaoShaOrder mOrder =  new MiaoShaOrder();
            BeanUtils.copyProperties(vo,mOrder);
            int result = mSLogic.insertMiaoshaOrder(mOrder);
            if(result<=0){
                log.error("***插入订单失败insertMiaoshaOrder*** error:{}", JSON.toJSON(vo));
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.FAILD);
                return resultGeekQ;
            }
            resultGeekQ.setData(result);
            return resultGeekQ;
        }catch(Exception e){
            log.error("***秒杀下订单失败*** error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return resultGeekQ;
        }
    }

    @Override
    public ResultGeekQ<MiaoShaOrderVo> getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId) {

        ResultGeekQ<MiaoShaOrderVo> resultGeekQ = ResultGeekQ.build();
        try{
            MiaoShaOrderVo mOrder =  new MiaoShaOrderVo();
            MiaoShaOrder mSorder = mSLogic.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
            BeanUtils.copyProperties(mSorder,mOrder);
            resultGeekQ.setData(mOrder);
            return resultGeekQ;
        }catch(Exception e){
            log.error("***查询失败getMiaoshaOrderByUserIdGoodsId *** error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return resultGeekQ;
        }
    }

    @Override
    public ResultGeekQ<String> createMiaoshaPath(MiaoShaUserVo user, Long goodsId) {
        ResultGeekQ<String> resultGeekQ = ResultGeekQ.build();
        try{
            if (user == null || goodsId <= 0) {
                return null;
            }
            String str = MD5Util.md5(UUIDUtil.getUUid() + "123456");
            log.info("createMiaoShaPath str:{}", str);
            redisClient.set(MiaoshaKey.getMiaoshaPath, "" + user.getNickname() + "_" + goodsId, str);
            resultGeekQ.setData(str);
            return resultGeekQ;
        }catch(Exception e){
            log.error("***查询失败getMiaoshaOrderByUserIdGoodsId *** error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return resultGeekQ;
        }
    }


    @Override
    public ResultGeekQ<Long> getMiaoshaResult(Long userId, Long goodsId) {
        ResultGeekQ<Long> resultGeekQ = ResultGeekQ.build();
        try{
            MiaoShaOrder order = mSLogic.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
            //秒杀成功
            if (order != null) {
                log.info("***getMiaoshaResult返回结果成功***");
                resultGeekQ.setData(order.getId());
                return resultGeekQ;
            } else {
                //此商品的秒杀已经结束，但是可能订单还在生成中
                //获取所有的秒杀订单, 判断订单数量和参与秒杀的商品数量
                List<MiaoShaOrder> orders = mSLogic.listByGoodsId(goodsId);
                if (orders == null) {
                    //订单还在生成中
                    resultGeekQ.setData(Long.valueOf(CustomerConstant.MS_ING));
                    return  resultGeekQ;
                } else {
                    //判断是否有此用户的订单
                    MiaoShaOrder orderIsGet = get(orders, userId);
                    //如果有，则说明秒杀成功
                    if (orderIsGet != null) {
                        resultGeekQ.setData( orderIsGet.getOrderId() );
                        return resultGeekQ;
                    } else {
                        //秒杀失败
                        resultGeekQ.setData(Long.valueOf(CustomerConstant.MS_F));
                        return resultGeekQ;
                    }
                }
            }
        }catch(Exception e){
            log.error("***getMiaoshaResult *** error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_RESULT_FAIL);
            return resultGeekQ;
        }
    }

    @Override
    public ResultGeekQ<Boolean> checkPath(MiaoShaUserVo user, long goodsId, String path) {
        ResultGeekQ<Boolean> resultGeekQ = ResultGeekQ.build();
        try{
            MiaoShaUser mSuser = new MiaoShaUser();
            BeanUtils.copyProperties(user,mSuser);
            Boolean  checkPathR= mSLogic.checkPath(mSuser, goodsId, path);
            resultGeekQ.setData(checkPathR);
            return resultGeekQ;
        }catch(Exception e){
            log.error("***查询失败 checkPath *** error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return resultGeekQ;
        }
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
