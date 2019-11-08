package com.travel.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 邱润泽 bullock
 */
@Controller
@RequestMapping("/register")
public class RegisterController {


    @RequestMapping(value = "index" ,produces = "text/html")
    public String index(){
        return "register";
    }

}
