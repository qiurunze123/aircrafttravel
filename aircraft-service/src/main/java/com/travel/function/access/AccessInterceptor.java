package com.travel.function.access;

import com.alibaba.fastjson.JSON;
import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.commons.utils.CookiesUtils;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.logic.MiaoShaLogic;
import com.travel.function.redisManager.RedisClient;
import com.travel.service.MiaoShaUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

import static com.travel.commons.enums.CustomerConstant.COOKIE_NAME_TOKEN;
import static com.travel.commons.enums.ResultStatus.ACCESS_LIMIT_REACHED;
import static com.travel.commons.enums.ResultStatus.SESSION_ERROR;

/**
 * @author 邱润泽 bullock
 */
@Service
@Slf4j
public class AccessInterceptor  extends HandlerInterceptorAdapter {


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
		 * 获取调用 获取主要方法  因为在 WebConfig 里面没有进行静态资源的排除
		 * 当然 你可以去排除 如果已经排除那就用不到这了  addPathPatterns("/**");
		 * so 在这里进行 静态资源handler 排除
		 */
		if(handler instanceof ResourceHttpRequestHandler) {
			log.info("---------ResourceHttpRequestHandler-------" + handler.toString() + "------------");
		}else if(handler instanceof HandlerMethod) {
			log.info("打印拦截方法handler ：{} ",handler);
			HandlerMethod hm = (HandlerMethod)handler;
			MiaoShaUser user = getUser(request, response);
			UserContext.setUser(user);
			UserCheckAndLimit accessLimit = hm.getMethodAnnotation(UserCheckAndLimit.class);
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
				log.info("****无需登录!**** 可直接访问");
			}

			//************************  设置redis等限流配置  **********************************
			AccessKey ak = AccessKey.withExpire(seconds);
			Integer count = (Integer) redisService.get(ak, key, Integer.class);
			if(count  == null) {
	    		 redisService.set(ak, key, 1);
	    	}else if(count < maxCount) {
	    		 redisService.incr(ak, key);
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

	//**** 回复 ****
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
		String cookieToken = CookiesUtils.getCookieValue(request, COOKIE_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return mSLogic.getByToken(response, token);
	}

//	private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
//			Cookie[] cookies = request.getCookies();
//			if(cookies == null){
//				log.error(" ***cookies 为null! 请登录***");
//				return null;
//			}
//			Cookie cookieValue =  Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieNameToken)).findFirst().get();
//			return cookieValue.getValue();
//	};

}
