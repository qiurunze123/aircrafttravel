package com.travel.service;


import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.vo.GoodsVo;

import java.util.List;

/**
 * @author 邱润泽 bullock
 */
public interface GoodsService {

    public ResultGeekQ<List<GoodsVo>> goodsVoList();

    public ResultGeekQ<GoodsVo> goodsVoByGoodId(Long goodId);

    public ResultGeekQ<Boolean> reduceStock(GoodsVo goods);
}
