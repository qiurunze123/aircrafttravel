package com.travel.web.controller;

import com.travel.commons.enums.CustomerConstant;
import com.travel.commons.resultbean.AbstractResult;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.commons.utils.ValidMSTime;
import com.travel.function.access.AccessLimit;
import com.travel.function.entity.MiaoShaMessage;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.rabbitmq.MQSender;
import com.travel.function.redisManager.RedisClient;
import com.travel.function.redisManager.keysbean.GoodsKey;
import com.travel.function.service.RandomValidateCodeService;
import com.travel.service.*;
import com.travel.vo.GoodsVo;
import com.travel.vo.MiaoShaOrderVo;
import com.travel.vo.MiaoShaUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.travel.commons.enums.CustomerConstant.MS_ING;
import static com.travel.commons.enums.ResultStatus.*;

@Controller
@RequestMapping("/miaosha")
@Slf4j
public class MiaoshaController {

    @Autowired
    MiaoShaUserService userService;

    @Autowired
    RedisClient redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RandomValidateCodeService codeService;
    @Autowired
    MQSender sender;
    @Autowired
    StringRedisTemplate redisTemplate;

    //商品售完标记map，多线程操作不能用HashMap
    private static ConcurrentHashMap<String, Boolean> productSoldOutMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Boolean> getProductSoldOutMap() {
        return productSoldOutMap;
    }


    @PostConstruct
    public void init()  throws Exception{
        ResultGeekQ<List<GoodsVo>> goodsListR = goodsService.goodsVoList();
        if(!ResultGeekQ.isSuccess(goodsListR)) {
            log.error("***系统初始化商品预热失败***");
            return;
        }
        List<GoodsVo> goodsList = goodsListR.getData();
        for(GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), goods.getStockCount());
            productSoldOutMap.put(String.valueOf(goods.getId()), false);
        }

    }


    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public ResultGeekQ<Long> miaoshaResult(Model model,MiaoShaUser user,
                                      @RequestParam("goodsId")long goodsId) {
        ResultGeekQ result =  ResultGeekQ.build();
        model.addAttribute("user", user);
        try {
            if(user == null) {
                result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
                return result;
            }
            ResultGeekQ<Long> response = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
            if(!ResultGeekQ.isSuccess(response)){
                result.withError(response.getCode(), response.getMessage());
                return result;
            }
            result.setData(response.getData());
        }catch (Exception e){
            result.withError(SYSTEM_ERROR);
            return result;
        }
        return result;
    }



    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value="/{path}/confirm", method= RequestMethod.POST)
    @ResponseBody
    public ResultGeekQ<Integer> miaosha(MiaoShaUser user, @PathVariable("path") String path,
                                        @RequestParam("goodsId") long goodsId) {
        ResultGeekQ<Integer> result = ResultGeekQ.build();
        try {
            if (user == null) {
                result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
                return result;
            }
            //验证path
            MiaoShaUserVo userVo = new MiaoShaUserVo();
            BeanUtils.copyProperties(user,userVo);
            ResultGeekQ<Boolean> check = miaoshaService.checkPath(userVo, goodsId, path);
            if (!ResultGeekQ.isSuccess(check)) {
                result.withError(REQUEST_ILLEGAL.getCode(), REQUEST_ILLEGAL.getMessage());
                return result;
            }
            //校验时间 防止刷时间
            ResultGeekQ<GoodsVo> goodR = goodsService.goodsVoByGoodId(Long.valueOf(goodsId));
            if(!ResultGeekQ.isSuccess(goodR)){
                result.withError(PRODUCT_NOT_EXIST.getCode(), PRODUCT_NOT_EXIST.getMessage());
                return result;
            }
            ResultGeekQ validR = ValidMSTime.validMiaoshaTime(goodR.getData());
            if(!ResultGeekQ.isSuccess(validR)){
                result.withError(validR.getCode(), validR.getMessage());
                return result;
            }

            //预减库存
            Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
            if (stock < 0) {
                result.withError(MIAO_SHA_OVER.getCode(), MIAO_SHA_OVER.getMessage());
                return result;
            }

            //是否已经秒杀到
            ResultGeekQ<MiaoShaOrderVo> order = orderService.getMiaoshaOrderByUserIdGoodsId(Long.valueOf(user.getNickname()), goodsId);
            if (!ResultGeekQ.isSuccess(order)) {
                result.withError(REPEATE_MIAOSHA.getCode(), REPEATE_MIAOSHA.getMessage());
                return result;
            }
            //入队
            MiaoShaMessage mm = new MiaoShaMessage();
            mm.setUser(user);
            mm.setGoodsId(goodsId);
            sender.sendMiaoshaMessage(mm);
            //正在进行中
            result.setData(MS_ING);
        } catch (Exception e) {
            result.withErrorCodeAndMessage(MIAOSHA_FAIL);
            return result;
        }
        return result;
    }


    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public ResultGeekQ<String> getMiaoshaVerifyCod(HttpServletResponse response, MiaoShaUser user,
                                                   @RequestParam("goodsId") long goodsId) {
        ResultGeekQ<String> result = ResultGeekQ.build();
        if (user == null) {
            result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
            return result;
        }
        try {
            BufferedImage image = codeService.getRandcode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return result;
        } catch (Exception e) {
            log.error("生成验证码错误-----goodsId:{}", goodsId, e);
            result.withError(MIAOSHA_FAIL.getCode(), MIAOSHA_FAIL.getMessage());
            return result;
        }
    }

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public ResultGeekQ<String> getMiaoshaPath(HttpServletRequest request, MiaoShaUser user,
                                              @RequestParam("goodsId") long goodsId,
                                              @RequestParam(value = "verifyCode", defaultValue = "0") String verifyCode
    ) {
        ResultGeekQ<String> result = ResultGeekQ.build();
        if (user == null) {
            result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
            return result;
        }

        MiaoShaUserVo userVo = new MiaoShaUserVo();
        BeanUtils.copyProperties(user,userVo);
        boolean check =codeService.checkVerifyCode(userVo, goodsId, verifyCode);
        if (!check) {
            result.withError(REQUEST_ILLEGAL.getCode(), REQUEST_ILLEGAL.getMessage());
            return result;
        }
        ResultGeekQ<String> pathR = miaoshaService.createMiaoshaPath(userVo, goodsId);
        if(!ResultGeekQ.isSuccess(pathR)){
            result.withError(pathR.getCode(), pathR.getMessage());
            return result;
        }
        result.setData(pathR.getData());
        return result;
    }

}
