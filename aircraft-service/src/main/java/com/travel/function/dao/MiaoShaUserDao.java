package com.travel.function.dao;

import com.travel.function.entity.MiaoShaUser;

public interface MiaoShaUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(MiaoShaUser record);

    int insertSelective(MiaoShaUser record);

    MiaoShaUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoShaUser record);

    int updateByPrimaryKey(MiaoShaUser record);

    MiaoShaUser getByName(String name);

}