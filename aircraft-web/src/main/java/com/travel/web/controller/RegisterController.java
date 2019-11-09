package com.travel.web.controller;

import com.alibaba.fastjson.JSON;
import com.travel.commons.enums.CustomerConstant;
import com.travel.commons.enums.MessageStatus;
import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.Response;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.function.entity.CustomerLogin;
import com.travel.function.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * @author 邱润泽 bullock
 */
@Controller
@RequestMapping("/register")
public class RegisterController {

    public  static Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "index" ,produces = "text/html")
    public String index(){
        return "register";
    }

    @RequestMapping(value = "do_register")
    @ResponseBody
    public ResultGeekQ<String> register(CustomerLogin login){
        ResultGeekQ<String> resultGeekQ = ResultGeekQ.build();
        try {
            login.setUserStats(CustomerConstant.LoginUserStatus.USER_ENABLE.getCode());
            login.setModifiedTime(new Date());
            customerService.insertCustomerLogin(login);

        }catch (Exception e){
            logger.error("register is error! error:{}",e);
            resultGeekQ.withError(ResultStatus.RESIGETER_FAIL);
        }
        return resultGeekQ;
    }

}
