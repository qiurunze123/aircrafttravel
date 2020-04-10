//package com.travel.function.iplimit;
//
//import com.alibaba.fastjson.JSON;
//import com.travel.commons.enums.ResultStatus;
//import com.travel.commons.resultbean.ResultGeekQ;
//import com.travel.function.access.AccessKey;
//import com.travel.function.access.UserCheckAndLimit;
//import com.travel.function.access.UserContext;
//import com.travel.function.entity.MiaoShaUser;
//import com.travel.function.logic.MiaoShaLogic;
//import com.travel.function.redisManager.RedisClient;
//import com.travel.function.redisManager.RedisLimitRateWithLUA;
//import com.travel.service.MiaoShaUserService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
//import redis.clients.jedis.Jedis;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.travel.commons.enums.CustomerConstant.COOKIE_NAME_TOKEN;
//import static com.travel.commons.enums.ResultStatus.ACCESS_LIMIT_REACHED;
//import static com.travel.commons.enums.ResultStatus.SESSION_ERROR;
//
///**
// * @author 邱润泽 bullock
// */
//@Service
//@Slf4j
//public class IpLimitInterceptor extends HandlerInterceptorAdapter {
//
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		/**
//		 * 获取调用 获取主要方法  因为在 WebConfig 里面没有进行静态资源的排除
//		 * 当然 你可以去排除 如果已经排除那就用不到这了  addPathPatterns("/**");
//		 * so 在这里进行 静态资源handler 排除
//		 */
//		if(handler instanceof ResourceHttpRequestHandler) {
//			log.info("---------ResourceHttpRequestHandler-------" + handler.toString() + "------------");
//		}else if(handler instanceof HandlerMethod) {
//			log.info("打印拦截方法handler ：{} ",handler);
//			HandlerMethod hm = (HandlerMethod)handler;
//			IpLimit ipLimit = hm.getMethodAnnotation(IpLimit.class);
//
//			String limitCount = ipLimit.limitCount();
//			String limitTime = ipLimit.limitTime();
//			String ipN = IpGetConfig.getIp(request);
//			String key = ipN + System.currentTimeMillis()/1000; // 当前秒
//			accquire(key,limitCount,limitTime);
//		}
//		return false;
//	}
//
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//		super.afterCompletion(request, response, handler, ex);
//	}
//
//
//
//	public static boolean accquire(String key ,String limitCount , String limitTime ) throws IOException, URISyntaxException {
//		Jedis jedis = new Jedis("127.0.0.1");
//		File luaFile = new File(RedisLimitRateWithLUA.class.getResource("/").toURI().getPath() + "limit.lua");
//		String luaScript = FileUtils.readFileToString(luaFile);
////		String key = "ip:" + System.currentTimeMillis()/1000; // 当前秒
////		String limit = "5"; // 最大限制
////		String limitTime = "1";
//		List<String> keys = new ArrayList<String>();
//		keys.add(key);
//		List<String> args = new ArrayList<String>();
//		args.add(limitCount);
//		args.add(limitTime);
//		Long result = (Long)(jedis.eval(luaScript, keys, args)); // 执行lua脚本，传入参数
//		return result == 1;
//	}
//
//
//
//}
