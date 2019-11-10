package com.travel.function.service.impl;

import com.travel.commons.enums.ResultStatus;
import com.travel.commons.redisManager.RedisClient;
import com.travel.commons.redisManager.keysbean.MiaoShaUserKey;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.commons.utils.MD5Util;
import com.travel.commons.utils.UUIDUtil;
import com.travel.function.dao.MiaoShaUserDao;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.service.MiaoShaUserService;
import com.travel.function.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import static com.travel.commons.enums.CustomerConstant.COOKIE_NAME_TOKEN;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/9
 */
@Service
public class MiaoShaUserServiceImpl implements MiaoShaUserService {

    @Autowired
    private MiaoShaUserDao miaoShaUserDao;
    @Autowired
    private RedisClient redisClient;

    @Override
    public MiaoShaUser getById(long id) {
        return miaoShaUserDao.selectByPrimaryKey(id);
    }

    @Override
    public ResultGeekQ<String> login(HttpServletResponse response, LoginVo loginVo) {

        ResultGeekQ resultGeekQ = ResultGeekQ.build();
        try {
            if (loginVo == null) {
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
                return resultGeekQ;
            }

            String mobile = loginVo.getMobile();
            String formPass = loginVo.getPassword();

            MiaoShaUser user = getByName(mobile);

            if (user == null) {
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.MOBILE_NOT_EXIST);
                return resultGeekQ;
            }
            String dbPass = user.getPassword();
            String saltDB = user.getSalt();

            String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
            if (!calcPass.equals(dbPass)) {
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.PASSWORD_ERROR);
                return resultGeekQ;
            }
            //返回页面token
            String token = UUIDUtil.getUUid();
            addCookie(response, token, user);
        } catch (Exception e) {
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
            return resultGeekQ;
        }
        return resultGeekQ;
    }

    public void addCookie(HttpServletResponse response, String token, MiaoShaUser user) {
        redisClient.set(MiaoShaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置有效期
        cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public MiaoShaUser getByName(String name) {
        return miaoShaUserDao.getByName(name);
    }

    @Override
    public MiaoShaUser getByToken(HttpServletResponse response , String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoShaUser user = (MiaoShaUser) redisClient.get(MiaoShaUserKey.token,token,MiaoShaUser.class);
        if(user!=null){
            addCookie(response,token,user);
        }
        return user;
    }
}
