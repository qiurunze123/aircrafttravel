package com.travel.function.dao;

import com.travel.function.entity.MiaoShaGoods;

public interface MiaoShaGoodsDao {
    int deleteByPrimaryKey(Long id);

    int insert(MiaoShaGoods record);

    int insertSelective(MiaoShaGoods record);

    MiaoShaGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoShaGoods record);

    int updateByPrimaryKey(MiaoShaGoods record);
}