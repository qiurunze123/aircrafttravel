package com.travel.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 邱润泽 bullock
 */
@Slf4j
public class CookiesUtils {

    public static String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        Optional cookiesV =  Optional.ofNullable(cookies);
        cookiesV.orElseGet(()->{
            log.error(" ***cookies 为null! 请登录***");
            return null;
        });

        if(!cookiesV.isPresent()){
            return null;
        }
        List<String> tokens =  Arrays.asList(cookies).stream().
                filter(cookie -> cookie.getName().equals(cookieNameToken))
                .map(cookie -> cookie.getValue())
                .collect(Collectors.toList());
        return CollectionUtils.isEmpty(tokens)?"":tokens.get(0);
    };
}
