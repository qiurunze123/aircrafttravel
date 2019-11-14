package com.travel.web.controller;

import com.travel.commons.enums.ResultStatus;
import com.travel.commons.redisManager.RedisClient;
import com.travel.commons.redisManager.RedisService;
import com.travel.commons.redisManager.keysbean.GoodsKey;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.function.entity.Goods;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.service.GoodsService;
import com.travel.function.service.MiaoShaUserService;
import com.travel.function.vo.GoodsDetailVo;
import com.travel.function.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
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
public class GoodsController extends BaseController {

    @Autowired
    private MiaoShaUserService miaoShaUserService;

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisClient redisService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_detail/{goodsId}")
    @ResponseBody
    public String goodsDetail(Model model, HttpServletRequest request, HttpServletResponse response, MiaoShaUser user,
                              @PathVariable(required = true) String goodsId) {
        model.addAttribute("user", user);
        //取缓存
        String html = (String) redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

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
        model.addAttribute("goods", goods);
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return render(request, response, model, "goods_detail", GoodsKey.getGoodsDetail, "");
    }

    @RequestMapping(value = "/to_list")
    @ResponseBody
    public String goodsList(HttpServletRequest request, HttpServletResponse response,
                            MiaoShaUser user, Model model) {
        model.addAttribute("user", user);
        List<GoodsVo> goods = goodsService.goodsVoList();
        model.addAttribute("goodsList", goods);
        return render(request, response, model, "goods_list", GoodsKey.getGoodsList, "");
    }
}
