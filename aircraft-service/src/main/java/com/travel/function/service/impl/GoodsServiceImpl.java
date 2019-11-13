package com.travel.function.service.impl;

import com.travel.function.dao.GoodsDao;
import com.travel.function.entity.MiaoShaGoods;
import com.travel.function.service.GoodsService;
import com.travel.function.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 邱润泽 bullock
 */
@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;
    @Override
    public List<GoodsVo> goodsVoList() {
        return goodsDao.goodsVoList();
    }

    @Override
    public GoodsVo goodsVoByGoodId(Long goodId) {
        return goodsDao.goodsVoByGoodsId(goodId);
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.goodsVoByGoodsId(goodsId);
    }

    @Override
    public boolean reduceStock(GoodsVo goods) {
        MiaoShaGoods g = new MiaoShaGoods();
        g.setGoodsId(goods.getId());
        return goodsDao.reduceStock(g)>0;
    }
}
