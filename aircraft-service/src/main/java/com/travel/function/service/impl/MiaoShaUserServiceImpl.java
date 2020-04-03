package com.travel.function.service.impl;

import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.commons.utils.MD5Util;
import com.travel.commons.utils.UUIDUtil;
import com.travel.function.dao.MiaoShaUserDao;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.logic.MiaoShaLogic;
import com.travel.function.redisManager.RedisClient;
import com.travel.service.MiaoShaUserService;
import com.travel.vo.LoginVo;
import com.travel.vo.MiaoShaUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/9
 */
@Service
@Slf4j
public class MiaoShaUserServiceImpl implements MiaoShaUserService {


    @Autowired
    private MiaoShaUserDao miaoShaUserDao;

    @Autowired
    private MiaoShaLogic mSLogic ;


    @Override
    public ResultGeekQ<MiaoShaUserVo> getById(Long id) {
        ResultGeekQ<MiaoShaUserVo> resultGeekQ  = ResultGeekQ.build();
        try{
            MiaoShaUser user = miaoShaUserDao.selectByPrimaryKey(id);
            MiaoShaUserVo userVo = new MiaoShaUserVo();
            BeanUtils.copyProperties(user,userVo);
            resultGeekQ.setData(userVo);
            return resultGeekQ;
        }catch(Exception e){
            log.error("***获取秒杀用户对象失败！getById*** error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return resultGeekQ;
        }
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
            ResultGeekQ<MiaoShaUserVo> userVo = getByName(mobile);
            if (!ResultGeekQ.isSuccess(userVo)) {
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.MOBILE_NOT_EXIST);
                return resultGeekQ;
            }
            MiaoShaUserVo user = userVo.getData();
            String dbPass = user.getPassword();
            String saltDB = user.getSalt();

            String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
            if (!calcPass.equals(dbPass)) {
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.PASSWORD_ERROR);
                return resultGeekQ;
            }
            //返回页面token
            String token = UUIDUtil.getUUid();
            MiaoShaUser mSuser = new MiaoShaUser();
            BeanUtils.copyProperties(user,mSuser);
            mSLogic.addCookie(response, token, mSuser);
        } catch (Exception e) {
            log.error("登陆发生错误 error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
            return resultGeekQ;
        }
        return resultGeekQ;
    }

    @Override
    public ResultGeekQ<MiaoShaUserVo> getByName(String name) {
        ResultGeekQ<MiaoShaUserVo> resultGeekQ  = ResultGeekQ.build();
        try{
            MiaoShaUser user = miaoShaUserDao.getByName(name);;
            MiaoShaUserVo userVo = new MiaoShaUserVo();
            BeanUtils.copyProperties(user,userVo);
            resultGeekQ.setData(userVo);
            return resultGeekQ;
        }catch(Exception e){
            log.error("***获取秒杀用户对象失败！getByName*** error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return resultGeekQ;
        }
    }



//    // http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
//    public boolean updatePassword(String token, String nickName, String formPass) {
//        //取user
//        MiaoShaUser user = getByName(nickName);
//        if(user == null) {
//            throw new GlobleException(MOBILE_NOT_EXIST);
//        }
//        //更新数据库
//        MiaoShaUser toBeUpdate = new MiaoShaUser();
//        toBeUpdate.setNickname(nickName);
//        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
//        miaoShaUserDao.updateByPrimaryKeySelective(toBeUpdate);
//        //处理缓存
//        redisClient.delete(MiaoShaUserKey.getByNickName, ""+nickName);
//        user.setPassword(toBeUpdate.getPassword());
//        redisClient.set(MiaoShaUserKey.token, token, user);
//        return true;
//    }

}
