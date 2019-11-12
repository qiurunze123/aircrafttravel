package com.travel.function.service.impl;

import com.travel.function.dao.UserDao;
import com.travel.function.entity.User;
import com.travel.function.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 邱润泽 bullock
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao ;

    @Override
    public User getUser(Integer id){
       return  userDao.getUser(id);
    };

}
