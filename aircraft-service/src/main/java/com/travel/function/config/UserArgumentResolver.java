package com.travel.function.config;

import com.travel.function.entity.MiaoShaUser;
import com.travel.function.service.MiaoShaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import static com.travel.commons.enums.CustomerConstant.COOKIE_NAME_TOKEN;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private MiaoShaUserService miaoShaUserService;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //获取参数类型
      Class<?> clazz =    methodParameter.getParameterType() ;
      return clazz == MiaoShaUser.class ;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        String cookieToken = getCookieValue(request,COOKIE_NAME_TOKEN);
        String paramToken = request.getParameter(COOKIE_NAME_TOKEN);

        if(StringUtils.isEmpty(cookieToken)&& StringUtils.isEmpty(paramToken)){
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        MiaoShaUser user = miaoShaUserService.getByToken(response,token);

        return user;

        /**
         *  threadlocal 存储线程副本 保证线程不冲突
         */
//        return UserContext.getUser();
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        Cookie cookieValue =  Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieNameToken)).findFirst().get();
        return cookieValue.getValue();
    };

}
