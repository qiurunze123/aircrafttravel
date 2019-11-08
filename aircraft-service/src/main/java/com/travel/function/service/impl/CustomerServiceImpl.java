package com.travel.function.service.impl;

import com.alibaba.fastjson.JSON;
import com.travel.function.dao.CustomerInfDao;
import com.travel.function.dao.CustomerLoginDao;
import com.travel.function.entity.CustomerInf;
import com.travel.function.entity.CustomerLogin;
import com.travel.function.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 邱润泽 bullock
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    public static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Autowired
    private CustomerInfDao customerInfDao;

    @Autowired
    private CustomerLoginDao customerLoginDao;

    @Override
    public void insertCustomerInf(CustomerInf record) throws Exception {
        logger.info("insert customerInf :{}", JSON.toJSON(record));
        int result =  customerInfDao.insertSelective(record);
        if(result<0){
            throw new Exception("插入用户基础信息失败");
        }
    }

    @Override
    public void insertCustomerLogin(CustomerLogin record) throws Exception {
        logger.info("insert CustomerLogin :{}", JSON.toJSON(record));
        int result = customerLoginDao.insertSelective(record);
        if(result<0){
            throw new Exception("插入用户注册信息失败");
        }
    }
}
