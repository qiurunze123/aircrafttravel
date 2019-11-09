package com.travel.function.service.impl;

import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.commons.utils.MD5Util;
import com.travel.function.dao.MiaoShaUserDao;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.service.MiaoShaUserService;
import com.travel.function.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/9
 */
@Service
public class MiaoShaUserServiceImpl implements MiaoShaUserService {

    @Autowired
    private MiaoShaUserDao miaoShaUserDao;

    @Override
    public MiaoShaUser getById(long id) {
        return miaoShaUserDao.selectByPrimaryKey(id);
    }

    @Override
    public ResultGeekQ<String> login(LoginVo loginVo) {

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
        } catch (Exception e) {
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
            return resultGeekQ;
        }
        return resultGeekQ;
    }

    @Override
    public MiaoShaUser getByName(String name) {
        return miaoShaUserDao.getByName(name);
    }
}
