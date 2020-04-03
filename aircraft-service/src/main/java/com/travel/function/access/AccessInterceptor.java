package com.travel.function.access;

import com.alibaba.fastjson.JSON;
import com.travel.commons.enums.ResultStatus;
import com.travel.function.logic.MiaoShaLogic;
import com.travel.function.redisManager.RedisClient;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.function.entity.MiaoShaUser;
import com.travel.service.MiaoShaUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Arrays;

import static com.travel.commons.enums.CustomerConstant.COOKIE_NAME_TOKEN;
import static com.travel.commons.enums.ResultStatus.ACCESS_LIMIT_REACHED;
import static com.travel.commons.enums.ResultStatus.SESSION_ERROR;


@Service
public class AccessInterceptor  extends HandlerInterceptorAdapter {

	private static Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);

	@Autowired
	MiaoShaUserService userService;

	@Autowired
	private MiaoShaLogic mSLogic;

	@Autowired
	RedisClient redisService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/**
		 * 获取调用 获取主要方法
		 */
		if(handler instanceof HandlerMethod) {
			logger.info("打印拦截方法handler ：{} ",handler);
			HandlerMethod hm = (HandlerMethod)handler;
			MiaoShaUser user = getUser(request, response);
			UserContext.setUser(user);
			AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
			if(accessLimit == null) {
				return true;
			}
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			String key = request.getRequestURI();
			if(needLogin) {
				if(user == null) {
					render(response, SESSION_ERROR);
					return false;
				}
				key += "_" + user.getNickname();
			}else {
			}
			AccessKey ak = AccessKey.withExpire(seconds);
//			Integer count = (Integer) redisService.get(ak, key, Integer.class);
			Integer count =0;
			if(count  == null) {
//	    		 redisService.set(ak, key, 1);
	    	}else if(count < maxCount) {
//	    		 redisService.incr(ak, key);
	    	}else {
	    		render(response, ACCESS_LIMIT_REACHED);
	    		return false;
	    	}
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
		UserContext.removeUser();
	}

	private void render(HttpServletResponse response, ResultStatus cm)throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		String str  = JSON.toJSONString(ResultGeekQ.error(cm));
		out.write(str.getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	private MiaoShaUser getUser(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(COOKIE_NAME_TOKEN);
		String cookieToken = getCookieValue(request, COOKIE_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return mSLogic.getByToken(response, token);
	}

	private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
		Cookie[] cookies = request.getCookies();
		Cookie cookieValue =  Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieNameToken)).findFirst().get();
		return cookieValue.getValue();
	};

}
