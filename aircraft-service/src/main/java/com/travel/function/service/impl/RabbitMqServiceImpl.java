package com.travel.function.service.impl;

import com.travel.commons.enums.CustomerConstant;
import com.travel.commons.enums.ProductSoutOutMap;
import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.commons.utils.CommonMethod;
import com.travel.function.entity.MiaoShaMessage;
import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.exception.MqOrderException;
import com.travel.function.logic.GoodsLogic;
import com.travel.function.logic.MiaoShaLogic;
import com.travel.function.rabbitmq.MQConfig;
import com.travel.function.redisManager.RedisClient;
import com.travel.function.redisManager.keysbean.GoodsKey;
import com.travel.function.zk.ZkApi;
import com.travel.service.MiaoshaService;
import com.travel.service.RabbitMqService;
import com.travel.vo.GoodsVo;
import com.travel.vo.MiaoShaUserVo;
import com.travel.vo.OrderInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 邱润泽 bullock
 */
@Service
@Slf4j
public class RabbitMqServiceImpl implements RabbitMqService {
    @Autowired
    GoodsLogic goodsLogic;
    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    private MiaoShaLogic mSLogic;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private ZkApi zkApi;

    @Override
    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        MiaoShaMessage mm = RedisClient.stringToBean(message, MiaoShaMessage.class);
        MiaoShaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();
        GoodsVo goods = goodsLogic.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        MiaoShaOrder order = mSLogic.getMiaoshaOrderByUserIdGoodsId(Long.valueOf(user.getNickname()),
                goodsId);
        if (order != null) {
            throw new MqOrderException(ResultStatus.GOOD_EXIST);
        }
        //减库存 下订单 写入秒杀订单
        MiaoShaUserVo userVo = new MiaoShaUserVo();
        BeanUtils.copyProperties(user, userVo);
        //秒杀失败
        ResultGeekQ<OrderInfoVo> msR = miaoshaService.miaosha(userVo, goods);
        if(!ResultGeekQ.isSuccess(msR)){
            //************************ 秒杀失败 回退操作 **************************************
            redisClient.incr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
            if (ProductSoutOutMap.productSoldOutMap.get(goodsId) != null) {
                ProductSoutOutMap.productSoldOutMap.remove(goodsId);
            }
            //修改zk的商品售完标记为false
            try {
                if (zkApi.exists(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(String.valueOf(goodsId)), true) != null) {
                    zkApi.updateNode(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(String.valueOf(goodsId)), "false");
                }
            } catch (Exception e1) {
                log.error("修改zk商品售完标记异常", e1);
            }
            return;
        }
        OrderInfoVo orderInfo = msR.getData();
        //******************  如果成功则进行保存redis + flag ****************************
        String msKey  = CommonMethod.getMiaoshaOrderRedisKey(String.valueOf(orderInfo.getUserId()), String.valueOf(goodsId));
        redisClient.set(msKey, msR.getData());

    }
}
