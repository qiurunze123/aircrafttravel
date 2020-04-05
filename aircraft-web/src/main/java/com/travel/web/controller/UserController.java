package com.travel.web.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 邱润泽 bullock
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value="/list", produces="text/html")
    @ResponseBody
    public String list() {
        List<String> list = Arrays.asList("1","2","3");
        List<String> list1 = list.stream().filter((String str)->
                str.equals("2")).collect(Collectors.toList());
        return JSON.toJSONString(list1);
    }
}
