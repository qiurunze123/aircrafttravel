package com.travel.web.controller;

import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.function.access.UserCheckAndLimit;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.redisManager.RedisClient;
import com.travel.function.redisManager.keysbean.GoodsKey;
import com.travel.service.GoodsService;
import com.travel.service.MiaoShaUserService;
import com.travel.vo.GoodsDetailVo;
import com.travel.vo.GoodsVo;
import com.travel.vo.MiaoShaUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.travel.commons.enums.CustomerConstant.MiaoShaStatus.MIAO_SHA_END;
import static com.travel.commons.enums.CustomerConstant.MiaoShaStatus.MIAO_SHA_NOT_START;

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

    @UserCheckAndLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/to_detail")
    @ResponseBody
    public ResultGeekQ<GoodsDetailVo> goodsDetail(MiaoShaUser user, String goodsId) {
        ResultGeekQ resultGeekQ = ResultGeekQ.build();
        try {
//            if(StringUtils.isEmpty(user.getNickname())){
//                resultGeekQ.withError(ResultStatus.USER_NOT_EXIST.getCode(),ResultStatus.USER_NOT_EXIST.getMessage());
//                return resultGeekQ;
//            }
            ResultGeekQ<GoodsVo> goodR = goodsService.goodsVoByGoodId(Long.valueOf(goodsId));
            if(!ResultGeekQ.isSuccess(goodR)){
                resultGeekQ.withError(goodR.getCode(),goodR.getMessage());
                return resultGeekQ;
            }
            GoodsVo goods = goodR.getData();
            long startAt = goods.getStartDate().getTime();
            long endAt = goods.getEndDate().getTime();
            long now = System.currentTimeMillis();
            int miaoshaStatus = 0;
            int remainSeconds = 0;
            //秒杀还没开始，倒计时
            if (now < startAt) {
                miaoshaStatus = MIAO_SHA_NOT_START.getCode();
                remainSeconds = (int) ((startAt - now) / 1000);
                //秒杀已经结束
            } else if (now > endAt) {
                miaoshaStatus = MIAO_SHA_END.getCode();
                remainSeconds = -1;
                //秒杀进行中
            } else {
                miaoshaStatus = MIAO_SHA_END.getCode();
                remainSeconds = 0;
            }
            GoodsDetailVo vo = new GoodsDetailVo();
            vo.setGoods(goods);
            MiaoShaUserVo userVo = new MiaoShaUserVo();
            BeanUtils.copyProperties(user,userVo);
            vo.setUser(userVo);
            vo.setRemainSeconds(remainSeconds);
            vo.setMiaoshaStatus(miaoshaStatus);
            resultGeekQ.setData(vo);
        } catch (Exception e) {
            log.error("秒杀明细请求失败 error:{}", e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return resultGeekQ;
        }
        return resultGeekQ;
    }

    @RequestMapping(value = "/to_list")
    @ResponseBody
    public String goodsList(HttpServletRequest request, HttpServletResponse response,
                            MiaoShaUser user, Model model) {
        model.addAttribute("user", user);
        ResultGeekQ<List<GoodsVo>> goodsR = goodsService.goodsVoList();
        if(!ResultGeekQ.isSuccess(goodsR)){
            //todo 如何处理
            return null;
        }


        model.addAttribute("goodsList", goodsR.getData());
        return render(request, response, model, "goods_list", GoodsKey.getGoodsList, "");
    }
}
