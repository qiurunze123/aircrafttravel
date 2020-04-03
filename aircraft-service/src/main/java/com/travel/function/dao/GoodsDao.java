package com.travel.function.dao;

import com.travel.function.entity.Goods;
import com.travel.function.entity.MiaoShaGoods;
import com.travel.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsDao {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Goods record);

     List<GoodsVo> goodsVoList();

    GoodsVo goodsVoByGoodsId(Long goodId);

    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    public int reduceStock(MiaoShaGoods miaoShaGoods);
}