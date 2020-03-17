package com.travel.web.controller;

import com.travel.commons.enums.ResultStatus;
import com.travel.function.rabbitmq.MiaoShaMessage;
import com.travel.function.redisManager.RedisClient;
import com.travel.function.redisManager.RedisService;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.function.access.AccessLimit;
import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.function.redisManager.keysbean.GoodsKey;
import com.travel.function.service.*;
import com.travel.function.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

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
    RabbitMqService mqService;
    @Autowired
    StringRedisTemplate redisTemplate;

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
            long response = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
            result.setData(response);
        }catch (Exception e){
            result.withError(SYSTEM_ERROR);
            return result;
        }
        return result;
    }

    /**
     * QPS:1306
     * 5000 * 10
     */
    /**
     * QPS:1306
     * 5000 * 10
     * get　post get 幂等　从服务端获取数据　不会产生影响　　post 对服务端产生变化
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value="/{path}/do_miaosha", method= RequestMethod.POST)
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
            boolean check = miaoshaService.checkPath(user, goodsId, path);
            if (!check) {
                result.withError(REQUEST_ILLEGAL.getCode(), REQUEST_ILLEGAL.getMessage());
                return result;
            }
            //是否已经秒杀到
            MiaoShaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(Long.valueOf(user.getNickname()), goodsId);
            if (order != null) {
                result.withError(REPEATE_MIAOSHA.getCode(), REPEATE_MIAOSHA.getMessage());
                return result;
            }
            //预减库存
            Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
            if (stock < 0) {
                result.withError(MIAO_SHA_OVER.getCode(), MIAO_SHA_OVER.getMessage());
                return result;
            }
            //排队
            MiaoShaMessage mm = new MiaoShaMessage();
            mm.setGoodsId(goodsId);
            mm.setUser(user);
            mqService.sendMiaoshaMessage(mm);
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
        boolean check =codeService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            result.withError(REQUEST_ILLEGAL.getCode(), REQUEST_ILLEGAL.getMessage());
            return result;
        }
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        result.setData(path);
        return result;
    }

}
