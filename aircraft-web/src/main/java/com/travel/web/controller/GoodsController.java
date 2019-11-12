package com.travel.web.controller;

import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.function.entity.Goods;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.service.GoodsService;
import com.travel.function.service.MiaoShaUserService;
import com.travel.function.vo.GoodsDetailVo;
import com.travel.function.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static com.travel.commons.enums.CustomerConstant.COOKIE_NAME_TOKEN;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/10  b7797cce01b4b131b433b6acf4add449
 */
@Controller
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Autowired
    private MiaoShaUserService miaoShaUserService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/to_detail/{goodsId}")
    public String goodsDetail(Model model, HttpServletResponse response,MiaoShaUser user,
                              @PathVariable(required = true) String goodsId) {

        ResultGeekQ<GoodsDetailVo> resultGeekQ = ResultGeekQ.build();
        try {
            GoodsVo goods = goodsService.goodsVoByGoodId(Long.valueOf(goodsId));
            long startAt = goods.getStartDate().getTime();
            long endAt = goods.getEndDate().getTime();
            long now = System.currentTimeMillis();
            int miaoshaStatus = 0;
            int remainSeconds = 0;
            //秒杀还没开始，倒计时
            if (now < startAt) {
                miaoshaStatus = 0;
                remainSeconds = (int) ((startAt - now) / 1000);
                //秒杀已经结束
            } else if (now > endAt) {
                miaoshaStatus = 2;
                remainSeconds = -1;
                //秒杀进行中
            } else {
                miaoshaStatus = 1;
                remainSeconds = 0;
            }
            GoodsDetailVo vo = new GoodsDetailVo();
            vo.setGoods(goods);
            vo.setUser(user);
            vo.setRemainSeconds(remainSeconds);
            vo.setMiaoshaStatus(miaoshaStatus);
            model.addAttribute("goods",goods);
            model.addAttribute("miaoshaStatus",miaoshaStatus);
            model.addAttribute("remainSeconds",remainSeconds);
            model.addAttribute("user",user);
//            resultGeekQ.setData(vo);
        } catch (Exception e) {
            log.error("查询商品明细失败 error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.GOOD_NOT_EXIST);
        }
        return "goods_detail" ;
    }

    @RequestMapping(value = "/to_list")
    public String goodsList(MiaoShaUser user, Model model) {
        model.addAttribute("user", user);
        List<GoodsVo> goods = goodsService.goodsVoList();
        model.addAttribute("goodsList", goods);
        return "goods_list";
    }
}
