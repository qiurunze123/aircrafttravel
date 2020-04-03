package com.travel.function.service.impl;

import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.logic.GoodsLogic;
import com.travel.function.logic.MiaoShaLogic;
import com.travel.function.rabbitmq.MQConfig;
import com.travel.function.rabbitmq.MiaoShaMessage;
import com.travel.function.redisManager.RedisClient;
import com.travel.service.MiaoshaService;
import com.travel.service.RabbitMqService;
import com.travel.vo.GoodsVo;
import com.travel.vo.MiaoShaUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
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
            return;
        }
        //todo 修改扣减方式 提醒错误
        //减库存 下订单 写入秒杀订单

        MiaoShaUserVo userVo = new MiaoShaUserVo();
        BeanUtils.copyProperties(user, userVo);
        miaoshaService.miaosha(userVo, goods);
    }
}
