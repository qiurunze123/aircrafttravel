package com.travel.function.dao;

import com.travel.function.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;

public interface OrderInfoDao {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    Long insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    public OrderInfo getOrderById(@Param("orderId")long orderId);
}