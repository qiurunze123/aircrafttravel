package com.travel.function.service;

import com.travel.function.entity.CustomerInf;
import com.travel.function.entity.CustomerLogin;
import org.springframework.stereotype.Service;

/**
 * @author 邱润泽 bullock
 */
public interface CustomerService {

    public void insertCustomerInf(CustomerInf record) throws Exception;

    public void insertCustomerLogin(CustomerLogin record) throws Exception;

}
