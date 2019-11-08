package com.travel.function.dao;

import com.travel.function.entity.CustomerInf;

public interface CustomerInfDao {
    int deleteByPrimaryKey(Integer customerInfId);

    int insertSelective(CustomerInf record);

    CustomerInf selectByPrimaryKey(Integer customerInfId);

    int updateByPrimaryKeySelective(CustomerInf record);

}