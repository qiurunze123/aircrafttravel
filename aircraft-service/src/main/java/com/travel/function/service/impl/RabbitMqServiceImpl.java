package com.travel.function.service.impl;

import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.rabbitmq.MQConfig;
import com.travel.function.rabbitmq.MiaoShaMessage;
import com.travel.function.redisManager.RedisClient;
import com.travel.function.service.GoodsService;
import com.travel.function.service.MiaoshaService;
import com.travel.function.service.OrderService;
import com.travel.function.service.RabbitMqService;
import com.travel.function.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 邱润泽 bullock
 */
@Service
@Slf4j
public class RabbitMqServiceImpl implements RabbitMqService {
    @Autowired
    RedisClient redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    AmqpTemplate amqpTemplate ;

    @Override
    @RabbitListener(queues= MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message:"+message);
        MiaoShaMessage mm  = RedisClient.stringToBean(message, MiaoShaMessage.class);
        MiaoShaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        MiaoShaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(Long.valueOf(user.getNickname()),
                goodsId);
        if(order != null) {
            return;
        }
        //todo 修改扣减方式 提醒错误
        //减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goods);
    }

    @Override
    public void sendMiaoshaMessage(MiaoShaMessage mm) {
        String msg = RedisClient.beanToString(mm);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }
}
