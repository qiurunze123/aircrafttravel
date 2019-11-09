package com.travel.web.controller;

import com.alibaba.fastjson.JSON;
import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.AbstractResult;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.commons.utils.PhoneUtil;
import com.travel.function.service.MiaoShaUserService;
import com.travel.function.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static com.travel.commons.enums.ResultStatus.MOBILE_ERROR;

/**
 * @author 邱润泽 bullock
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    public static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MiaoShaUserService miaoShaUserService;

    @RequestMapping("/to_login")
    public String tologin() {
        return "login";
    }
    @RequestMapping("/do_login")
    @ResponseBody
    public ResultGeekQ<String> tologin(@Valid LoginVo loginVo) {
        logger.info("登录开始 start! loginvo:{}", JSON.toJSON(loginVo));
        ResultGeekQ resultGeekQ = ResultGeekQ.build();
        try {
            ResultGeekQ result = miaoShaUserService.login(loginVo);
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
