package com.travel.web.controller;

import com.travel.function.entity.MiaoShaUser;
import com.travel.function.service.MiaoShaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

import static com.travel.commons.enums.CustomerConstant.COOKIE_NAME_TOKEN;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/10
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private MiaoShaUserService miaoShaUserService;

    @RequestMapping(value = "/to_list")
    public String goodsList(MiaoShaUser user, Model model) {
        model.addAttribute("user", user);
        return "goods_list";
    }

    public String goodsDetail(Model model, HttpServletResponse response,
                              @CookieValue(value = COOKIE_NAME_TOKEN, required = false) String cookieToken,
                              @RequestParam(value = COOKIE_NAME_TOKEN, required = false) String paramToken) {

        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        MiaoShaUser user = miaoShaUserService.getByToken(response, token);
        model.addAttribute("user", user);
        return "goods_list";
    }
}
