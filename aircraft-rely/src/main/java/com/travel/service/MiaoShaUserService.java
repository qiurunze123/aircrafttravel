package com.travel.service;

import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.vo.LoginVo;
import com.travel.vo.MiaoShaUserVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/9
 */
public interface MiaoShaUserService {

    public ResultGeekQ<MiaoShaUserVo> getById(Long id);

    public ResultGeekQ<String> login(HttpServletResponse response, LoginVo loginVo);

    ResultGeekQ<MiaoShaUserVo> getByName(String name);
}
