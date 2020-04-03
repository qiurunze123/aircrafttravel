package com.travel.web.controller;

import com.alibaba.fastjson.JSON;
import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.AbstractResult;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.service.MiaoShaUserService;
import com.travel.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author 邱润泽 bullock
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {


    @Autowired
    private MiaoShaUserService miaoShaUserService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/to_login")
    public String tologin() {
        return "login";
    }



    @RequestMapping("/do_login")
    @ResponseBody
    public ResultGeekQ<String> tologin(@Valid LoginVo loginVo, HttpServletResponse response) {
        log.info("登录开始 start! loginvo:{}", JSON.toJSON(loginVo));
        ResultGeekQ resultGeekQ = ResultGeekQ.build();
        try {
            ResultGeekQ result = miaoShaUserService.login(response,loginVo);
            if(!AbstractResult.isSuccess(result)){
                resultGeekQ.withError(result.getCode(),result.getMessage());
                return resultGeekQ;
            }
        } catch (Exception e) {
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
        }
        return resultGeekQ;
    }

}
