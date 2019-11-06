package com.travel.function.service.impl;

import com.travel.function.dao.UserDao;
import com.travel.function.entity.User;
import com.travel.function.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 邱润泽 bullock
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao ;

    @Override
    public User getUser(Integer id){
       return  userDao.getUser(id);
    };

}
