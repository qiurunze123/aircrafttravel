package com.travel.function.logic.impl;

import com.travel.function.dao.GoodsDao;
import com.travel.function.dao.MiaoShaOrderDao;
import com.travel.function.dao.MiaoShaUserDao;
import com.travel.function.dao.OrderInfoDao;
import com.travel.function.entity.MiaoShaGoods;
import com.travel.function.entity.MiaoShaOrder;
import com.travel.function.entity.MiaoShaUser;
import com.travel.function.entity.OrderInfo;
import com.travel.function.logic.GoodsLogic;
import com.travel.function.logic.MiaoShaLogic;
import com.travel.function.redisManager.RedisClient;
import com.travel.function.redisManager.keysbean.MiaoShaUserKey;
import com.travel.function.redisManager.keysbean.MiaoshaKey;
import com.travel.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

import static com.travel.commons.enums.CustomerConstant.COOKIE_NAME_TOKEN;

/**
 * @author 邱润泽 bullock
 */
@Slf4j
@Service
public class GoodsLogicImpl implements GoodsLogic {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public List<GoodsVo> goodsVoList() {
        return goodsDao.goodsVoList();
    }

    @Override
    public GoodsVo goodsVoByGoodsId(Long goodId) {
        return goodsDao.goodsVoByGoodsId(goodId);
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(Long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    @Override
    public int reduceStock(MiaoShaGoods miaoShaGoods) {
        return goodsDao.reduceStock(miaoShaGoods);
    }
}
