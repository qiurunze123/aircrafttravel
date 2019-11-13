package com.travel.web.controller;

import com.travel.commons.enums.ResultStatus;
import com.travel.commons.redisManager.RedisService;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.function.service.GoodsService;
import com.travel.function.service.MiaoShaUserService;
import com.travel.function.service.MiaoshaService;
import com.travel.function.service.OrderService;
import com.travel.function.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/miaosha")
@Slf4j
public class MiaoshaController {

    @Autowired
    MiaoShaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    /**
     * QPS:1306
     * 5000 * 10
     */
    @RequestMapping("/do_miaosha")
    public String list(Model model, MiaoShaUser user,
                       @RequestParam("goodsId") long goodsId) {

        try {
            model.addAttribute("user", user);
            if (user == null) {
                return "login";
            }
            //判断库存
            GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
            int stock = goods.getStockCount();
            if (stock <= 0) {
                model.addAttribute("errmsg", ResultStatus.MIAOSHA_FAIL.getMessage());
                return "miaosha_fail";
            }
            //判断是否已经秒杀到了
            MiaoShaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
            if (order != null) {
                model.addAttribute("errmsg", ResultStatus.MIAOSHA_FAIL.getMessage());
                return "miaosha_fail";
            }
            //减库存 下订单 写入秒杀订单
            OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
            model.addAttribute("orderInfo", orderInfo);
            model.addAttribute("goods", goods);
        } catch (Exception e) {
            log.error("秒杀失败 error:{}", e);
        }
        return "order_detail";
    }
}
