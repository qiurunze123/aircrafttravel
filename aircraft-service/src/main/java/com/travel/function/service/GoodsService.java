package com.travel.function.service;

import com.travel.function.vo.GoodsVo;

import java.util.List;

/**
 * @author 邱润泽 bullock
 */
public interface GoodsService {

    public List<GoodsVo> goodsVoList();

    public GoodsVo goodsVoByGoodId(Long goodId);
}
