package com.travel.function.dao;

import com.travel.function.entity.MiaoShaOrder;
import org.apache.ibatis.annotations.Param;

public interface MiaoShaOrderDao {
    int deleteByPrimaryKey(Long id);

    int insertSelective(MiaoShaOrder record);

    MiaoShaOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoShaOrder record);

    public MiaoShaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

}