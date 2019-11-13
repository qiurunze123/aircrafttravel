package com.travel.function.service.impl;

import com.travel.function.dao.OrderInfoDao;
import com.travel.function.entity.OrderInfo;
import com.travel.function.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 邱润泽 bullock
 */
@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderInfoDao orderInfoDao ;

    @Override
    public int insertSelective(OrderInfo record) {
        return orderInfoDao.insertSelective(record);
    }
}
