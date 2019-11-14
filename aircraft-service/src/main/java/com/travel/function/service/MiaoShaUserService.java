package com.travel.function.service;

import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/9
 */
public interface MiaoShaUserService {

    public MiaoShaUser getById(long id);

    public ResultGeekQ<String> login(HttpServletResponse response,LoginVo loginVo);

    MiaoShaUser getByName(String name);

    MiaoShaUser getByToken(HttpServletResponse response,String token);
}
