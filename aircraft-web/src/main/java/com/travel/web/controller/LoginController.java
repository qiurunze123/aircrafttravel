package com.travel.web.controller;

import com.alibaba.fastjson.JSON;
import com.travel.function.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 邱润泽 bullock
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    public static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/to_login")
    public String tologin(LoginVo loginVo) {
        logger.info("登录开始 start! loginvo:{}", JSON.toJSON(loginVo));



        return "login";
    }
}
