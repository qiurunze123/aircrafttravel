package com.travel.function.dao;


import com.travel.function.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    public User getUser(Integer id);

    public int insert(User user);

    public int update(User user);

    public int delete(Integer id);

}
