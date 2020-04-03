package com.travel.service;


import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.vo.OrderInfoVo;

/**
 * @author 邱润泽 bullock
 */
public interface OrderInfoService {
    ResultGeekQ<Integer> insertSelective(OrderInfoVo record);
}
